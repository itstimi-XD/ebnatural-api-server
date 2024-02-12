package ebnatural.bizcurator.apiserver.common.exception.custom;

/**
 * 이미 등록된 유저를 재등록하려고 할때 발생하는 Exception
 */
public class TermsOfServiceAgreementNeedException extends CustomException {


    public TermsOfServiceAgreementNeedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
