package open.microservice.accountmanagement.povisioner;


import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;

public interface IProvisioner {

    boolean canProvisioning(String value);

    void provisioning(ExternalOrder externalOrder);

}
