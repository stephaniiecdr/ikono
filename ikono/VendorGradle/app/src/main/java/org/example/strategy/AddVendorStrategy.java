package org.example.strategy; 

import org.example.Vendor; 
import org.hibernate.Session; 
import org.hibernate.exception.ConstraintViolationException; 


public class AddVendorStrategy implements VendorOperationStrategy { 
    @Override
    public void execute(Session session, Vendor vendor) throws Exception { 
        try {
            session.save(vendor); 
        } catch (ConstraintViolationException e) { 
            throw new Exception("Data (e.g., Name or Email) might already exist or violates database constraints.", e); 
        }
    }
}
