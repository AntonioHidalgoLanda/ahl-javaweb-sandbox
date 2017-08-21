/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import service.EndUserService;

/**
 *
 * @author antonio
 */
public class SessionMediator {
    final static String USERID_SESSION_NAME = "enduserid";
    private static SessionMediator singleton;
    
    private SessionMediator(){
        
    }
    
    static public SessionMediator getSessionMediator() {
        if (singleton == null){
            singleton = new SessionMediator();
        }
        return singleton;
    }
    
    public static String getOAuthUserName(){
      String username = "";
      Authentication oauth = SecurityContextHolder.getContext().getAuthentication();
      if (oauth instanceof OAuth2Authentication){
          Object od = ((OAuth2Authentication)oauth).getUserAuthentication().getDetails();
          if (od instanceof Map){
              username = ((Map)od).get("name").toString();
          }
      }
      return username;
    }
    
    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true == allow create
    }
    
    public static String sessionToString(){
        HttpSession httpSession = session();
        
        String s = "";
        for (Enumeration<String> e = httpSession.getAttributeNames(); e.hasMoreElements();){
            String n = (e.nextElement());
            s+=n+":"+httpSession.getAttribute(n).toString()+"; ";
        }
        
        return s;
    }
    
    public SessionMediator validateSessionUser() {
        HttpSession httpSession = session();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)
                && authentication.isAuthenticated()) {
            String currentUserName = authentication.getName();
            
            httpSession.setAttribute(USERID_SESSION_NAME, currentUserName);
            
            EndUserService endusers = new EndUserService();
            List<Map<String, Object>> lusers =
                    endusers.find(-1, currentUserName, "", "", "", false);
            int userid = -1;
            switch (lusers.size()){
                case 0:
                    String name = getOAuthUserName();
                    userid = Integer.parseInt(
                            endusers.upsert(-1, currentUserName, name, "", "")
                    );
                    break;
                case 1:
                    userid = (int) lusers.get(0).get("id");
                    break;
                default:
                    System.err.println("ERROR: We found duplicates users during login ("+currentUserName+")");
            }
            httpSession.setAttribute(USERID_SESSION_NAME, userid);
            
            
        }
        return this;
    }
}
