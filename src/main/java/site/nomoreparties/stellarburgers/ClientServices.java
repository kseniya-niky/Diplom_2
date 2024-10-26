package site.nomoreparties.stellarburgers;

import org.apache.commons.lang3.StringUtils;

public class ClientServices {
    public String trimAccessToken (String accessToken) {
        return StringUtils.substringAfter(accessToken, " ");
    }
}