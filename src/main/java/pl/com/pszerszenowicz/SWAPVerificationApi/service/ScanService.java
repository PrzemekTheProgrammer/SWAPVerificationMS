package pl.com.pszerszenowicz.SWAPVerificationApi.service;

import org.springframework.stereotype.Service;
import pl.com.pszerszenowicz.model.VerificationStatus;

@Service
public class ScanService {

    public VerificationStatus processScan(String scan) {
        return switch (scan.charAt(0)) {
            case '0','1','2' -> VerificationStatus.approved;
            case '3','4','5' -> VerificationStatus.outOfDate;
            case '6','7','8','9' -> VerificationStatus.released;
            default -> VerificationStatus.verificationError;
        };
    }


}
