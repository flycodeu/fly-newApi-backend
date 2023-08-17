package com.flyinterface.Test;

import com.flySdk.client.FlyApiClient;
import com.flySdk.model.User;

public class TestApiClient {
    public static void main(String[] args) {
        String accessKey = "5a8bd83a0019aea1b4caa846bac07426";
        String secretKey = "b55db76db7b87adac5df9215dc0afcb8";

        FlyApiClient flyApiClient = new FlyApiClient(accessKey, secretKey);

//        String get_fly = flyApiClient.getNameByGet("get_fly");
//        System.out.println(get_fly);
//        System.out.println("---------------------------");
//        String post_param_fly = flyApiClient.getNameByPost("post_param_fly");
//        System.out.println(post_param_fly);
//        System.out.println("--------------------------");

        User user = new User();
        user.setName("fly");
        String nameByPostJson = flyApiClient.getNameByPostJson(user);
        System.out.println(nameByPostJson);

    }
}
