package mc.project.customer.decoder;

import feign.Response;
import feign.codec.ErrorDecoder;
import mc.project.customer.exception.BadRequestException;

public class FraudErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        switch (response.status()) {
            case 400:
                return new BadRequestException("Bad request error from fraud service.");
            default:
                return new Exception("Fraud service error.");
        }
    }
}
