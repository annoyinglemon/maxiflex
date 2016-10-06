package ph.com.medilink.maxiflexmobileapp.WebServices;

/**
 * Created by nino_sandajan on 4/15/2016.
 */
public class ServiceParametersClass {

    public String Service_namespace() {
        return "http://tempuri.org/";
    }

    public String Service_url() {
        //Test
//        return "http://172.16.24.94/svc/DontiaChinaData.svc?wsdl";
//        return "http://10.0.2.2:36090/DontiaChinaData.svc";
        return "http://maxiflexproxytest.azurewebsites.net/MaxiFlexData.svc?wsdl";

        //UAT

        //Prod

    }

    public String Service_soap_action(String namespace, String methodname) {
        return namespace + "IMaxiFlexData/" + methodname;
    }

}
