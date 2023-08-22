package com.flyinterface.controller;

import com.flyCommon.common.BaseResponse;
import com.flyCommon.common.ErrorCode;
import com.flyCommon.common.ResultUtils;
import com.flyCommon.exception.BusinessException;
import com.flyinterface.entity.Request.BinaryConversionRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping( "/binary" )
@RestController
public class BinaryConversionController {
    public static final String[] F = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};

    @PostMapping( "/conversion" )
    public BaseResponse<String> binaryConversion(@RequestBody BinaryConversionRequest binaryConversionRequest) {
        if (binaryConversionRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long targetBinary = binaryConversionRequest.getTargetBinary();
        Long originalNumber = binaryConversionRequest.getOriginalNumber();
        if (targetBinary < 0 || targetBinary > 16) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        boolean flag = true;
        if (originalNumber < 0) {
            flag = false;
            originalNumber *= -1;
        }
        StringBuilder sb = new StringBuilder();

        if (originalNumber == 0) {
            sb.append("0");
        } else {
            while (originalNumber != 0) {
                long temp = originalNumber % targetBinary;
                sb.append(F[(int) temp]);
                originalNumber = originalNumber / targetBinary;
            }
        }
        if (!flag) {
            sb.append("-");
        }

        String res = sb.reverse().toString();
        return ResultUtils.success(res);
    }
}
