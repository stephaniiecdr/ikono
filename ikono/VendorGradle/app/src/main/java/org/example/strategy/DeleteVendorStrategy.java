package org.example.strategy; 

import org.example.Vendor; 
import org.hibernate.Session; 

public class DeleteVendorStrategy implements VendorOperationStrategy { 
    @Override
    public void execute(Session session, Vendor vendor) throws Exception { 
        session.delete(vendor); 
    }
}
