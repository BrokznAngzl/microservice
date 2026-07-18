package open.microservice.accountmanagement.povisioner;


import open.microservice.accountmanagement.model.hibernate.om.ExternalOrder;

public interface IProvisioner {

    public boolean canProvisioning(String value);

    public void provisioning(ExternalOrder externalOrder);

    public void resend(ExternalOrder externalOrder);

}
