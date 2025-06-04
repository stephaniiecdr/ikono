package org.example.strategy; 

import org.example.Vendor; 
import org.hibernate.Session; 


public interface VendorOperationStrategy { 
    void execute(Session session, Vendor vendor) throws Exception; 
}
