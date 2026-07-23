package open.microservice.accountmanagement.http;

import lombok.extern.slf4j.Slf4j;
import open.microservice.accountmanagement.model.exception.ProvisioningFailedException;
import open.microservice.accountmanagement.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.net.http.HttpResponse;
import java.time.Duration;

import static open.microservice.accountmanagement.constant.ErrorConstant.PROVISIONING_FAILED;
import static open.microservice.accountmanagement.constant.ErrorConstant.PROVISIONING_FAILED_DETAIL;
import static open.microservice.accountmanagement.constant.ExternalNodeConstant.PMD;

@Slf4j
@Service
public class PaymentHttpClient extends BaseHttpClient {

    public PaymentHttpClient(@Value("${app-config.pmd.conTimeout}") int connectTimeout,
                             @Value("${app-config.pmd.readTimeout}") int readTimeout) {

        super(Duration.ofSeconds(connectTimeout), Duration.ofSeconds(readTimeout));
    }

    public String callPayment(String endpoint, String requestBody) {
        HttpResponse<String> response = null;

        try {
            response = super.post(endpoint, requestBody);

            if (HttpStatus.OK.value() == response.statusCode()) {
                return response.body();

            } else {
                throw new ProvisioningFailedException(PROVISIONING_FAILED, StringUtil.format(PROVISIONING_FAILED_DETAIL, PMD, response.body()));
            }

        } catch (ProvisioningFailedException e) {
            throw e;

        } catch (Exception e) {
            throw new ProvisioningFailedException(PROVISIONING_FAILED, StringUtil.format(PROVISIONING_FAILED_DETAIL, PMD, e.getMessage()));

        } finally {
            log.info("request to {}: {}", endpoint, response);
        }

    }


}