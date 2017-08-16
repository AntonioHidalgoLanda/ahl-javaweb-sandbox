/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import datamediator.DataSourceSingleton;
import datamediator.PostgreSQLMediator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author antonio
 */
@RestController
public class PhotoService {
    
    BasicDataSource connectorPool = null;
    
    private final String UPLOADED_FOLDER = "photos/"; // = "F://temp//";
    
    PhotoService(){
        try {
            this.connectorPool = DataSourceSingleton.getConnectionPool();
        } catch (SQLException | URISyntaxException ex) {
            System.err.println(ex);
        }
    }
    
    
    @RequestMapping(path = "/download", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(String param) throws IOException {
        File file = new File(UPLOADED_FOLDER+"coat-of-arms.png");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    
    }
    
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadfile,
           @RequestParam(value="igotitId", required=false, defaultValue="0") int igotitId
            ) {
        if (uploadfile.isEmpty()) {
            return new ResponseEntity("please select a file!", HttpStatus.OK);
        }

        try {
            saveUploadedFiles(Arrays.asList(uploadfile));

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Successfully uploaded - " +
                uploadfile.getOriginalFilename(), new HttpHeaders(), HttpStatus.OK);
    }
    
    // TODO: This requires Refactoring into a File Mediator
    // fields: UPLOADED_FOLDER
    // method, return localpaths
    private void saveUploadedFiles(List<MultipartFile> files) throws IOException {

        for (MultipartFile file : files) {

            if (file.isEmpty()) {
                continue;
            }

            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);
        }

    }
    
    @RequestMapping(value = "/uploadStatus", method = RequestMethod.GET)
    public String uploadStatus() {
        return "uploadStatus";
    }
    
    @RequestMapping(value = "/photos", method = RequestMethod.GET)
    public @ResponseBody List<Map<String, Object>> find(
            @RequestParam(value="id", required=false, defaultValue="-1") int photoID,
           @RequestParam(value="localpath", required=false, defaultValue="") String localpath,
           @RequestParam(value="igotitId", required=false, defaultValue="0") int igotitId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("photo")
                .addFindField("id")
                .addFindField("localpath")
                .addFindField("igotitId");
        if (photoID >= 0){
            sm.addFindParam("id", photoID, 1);
        }
        if (!localpath.isEmpty()){
            sm.addFindParam("localpath", localpath, 1);
        }
        if (igotitId > 0){
            sm.addFindParam("igotitId", igotitId, 1);
        }
        sm.runFind();
        return sm.getResultsFind();
    }
    
    @RequestMapping(value = "/photo", method = RequestMethod.POST)
    public @ResponseBody String upsert(
            @RequestParam(value="id", required=false, defaultValue="-1") int photoID,
           @RequestParam(value="localpath", required=false, defaultValue="") String localpath,
           @RequestParam(value="igotitId", required=false, defaultValue="0") int igotitId
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("photo");
        if (photoID >= 0){
            sm.addId(photoID);
        }
        if (!localpath.isEmpty()){
            sm.addUpsertParam("localpath", localpath);
        }
        if (igotitId > 0){
            sm.addUpsertParam("igotitId", igotitId);
        }
        sm.runUpsert();
        return sm.getId();
    }
    
    
    @RequestMapping(value = "/photo", method = RequestMethod.DELETE)
    public @ResponseBody String delete(
            @RequestParam(value="id", required=true) int photoID
    ){
        PostgreSQLMediator sm = new PostgreSQLMediator(this.connectorPool);
        sm.setTable("photo")
                .addFindParam("id", photoID, 1);
        sm.runDelete();
        return ""+photoID;
    }
}
