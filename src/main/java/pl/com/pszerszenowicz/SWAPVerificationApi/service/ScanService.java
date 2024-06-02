package pl.com.pszerszenowicz.SWAPVerificationApi.service;

import org.springframework.stereotype.Service;
import pl.com.pszerszenowicz.model.VerificationStatus;

@Service
public class ScanService {

    public VerificationStatus processScan(String scan) {
        return switch (nullableCharAt(scan, 0)) {
            case "0", "1" -> VerificationStatus.ready;
            case "2", "3" -> VerificationStatus.sample;
            case "4", "5" -> VerificationStatus.expired;
            case "6", "7" -> VerificationStatus.released;
            case "8", "9" -> VerificationStatus.destroyed;
            default -> VerificationStatus.verificationError;
        };
    }

    private String nullableCharAt(String s, int index) {
        try {
            return String.valueOf(s.charAt(index));
        } catch (StringIndexOutOfBoundsException e) {
            return "";
        }
    }


}
