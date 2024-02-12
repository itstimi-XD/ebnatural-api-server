package ebnatural.bizcurator.apiserver.dto;

import lombok.Getter;

@Getter
public class AdminUserInfoDto {

    private String userName;
    private String businessName;
    private String businessNumber;
    private String managerPhoneNumber;
    private String manager;
    private String address;

    private AdminUserInfoDto(String userName, String businessName, String businessNumber,
            String managerPhoneNumber, String manager, String address) {
        this.userName = userName;
        this.businessName = businessName;
        this.businessNumber = businessNumber;
        this.managerPhoneNumber = managerPhoneNumber;
        this.manager = manager;
        this.address = address;
    }

    public static AdminUserInfoDto of(String userName, String businessName, String businessNumber,
            String managerPhoneNumber, String manager, String address) {
        return new AdminUserInfoDto(userName, businessName, businessNumber, managerPhoneNumber, manager, address);
    }
}
