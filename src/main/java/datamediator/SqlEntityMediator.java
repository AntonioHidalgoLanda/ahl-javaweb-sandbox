/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamediator;

import java.util.List;

/**
 *
 * @author antonio
 */
public interface SqlEntityMediator {
    SqlMediator getSqlMediator();
    SqlEntityMediator grantAccess(int entityId, List<Integer> listUsers);
    SqlEntityMediator grantAccess(int entityId);
    SqlEntityMediator revokeAccess(int entityId, List<Integer> listUsers);
    SqlEntityMediator revokeAccess(int entityId);
    
}
