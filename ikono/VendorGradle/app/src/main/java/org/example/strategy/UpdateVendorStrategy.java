package org.example.strategy; 

import org.example.Vendor; 
import org.hibernate.Session; 
import org.hibernate.exception.ConstraintViolationException; 

public class UpdateVendorStrategy implements VendorOperationStrategy { 
    @Override
    public void execute(Session session, Vendor vendor) throws Exception { 
        try {
            session.update(vendor); //
        } catch (ConstraintViolationException e) { 
            throw new Exception("Data (e.g., Name or Email) might already exist or violates database constraints.", e); //
        }
    }
}
