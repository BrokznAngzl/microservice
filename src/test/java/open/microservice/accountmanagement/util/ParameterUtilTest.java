package open.microservice.accountmanagement.util;


import open.microservice.accountmanagement.model.hibernate.OrderPropertyInformation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ParameterUtilTest {
    private OrderPropertyInformation op = new OrderPropertyInformation();
    private final ObjectMapper mapper = new ObjectMapper();
    private ParameterUtil parameterUtil;

    @BeforeEach
    void setUp() {
        op = op.mockUp();
        String jsonModel = mapper.writeValueAsString(op);
        parameterUtil = new ParameterUtil(jsonModel);
    }

    @Test
    void testPing() {
        String pingResult = parameterUtil.getMethodValue("ping()");
        assertEquals("pong", pingResult);
    }

    @Test
    void testGetJsonValue() {
        String accountNo = parameterUtil.getJsonValue("$.orderItem.account.accountNo");
        String accountId = parameterUtil.getJsonValue("$.orderItem.account.id");
        String invalidPath = parameterUtil.getJsonValue("$.soDeaMaCom");

        assertEquals("62142_mock", accountNo);
        assertEquals("1234", accountId);
        assertNull(invalidPath);
    }

    @Test
    void testGetMethodValue() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sdf.format(new Date());
        String mappingDate = parameterUtil.getMethodValue("getCurrentDate(yyyy-MM-dd)");
        assertEquals(currentDate, mappingDate);
    }

    @Test
    void testConvertDate() {
        String createDate = parameterUtil.getMethodValue("convertDate($.orderItem.account.createDate,yyyy/MM/dd HH:mm:ss,dd/MM/yyyy)");
        String invalidMethodResult = parameterUtil.getMethodValue("convertData($.orderItem.account.createDate,yyyy/MM/dd HH:mm:ss,dd/MM/yyyy)");

        assertEquals("11/08/2025", createDate);
        assertNull(invalidMethodResult);
    }

    @Test
    void testConcat() {
        String helloWord = parameterUtil.getMethodValue("concat(hello world, Beng)");
        String imSoLonely = parameterUtil.getMethodValue("concat(I\'m ,so, lonely)");
        String accountNo = parameterUtil.getMethodValue("concat(accountNo ,$.orderItem.account.accountNo)");
        String invalidPath = parameterUtil.getMethodValue("concat(homeNumber ,$.soDeaMaCom.account.address.homeNumber)");

        assertEquals("hello world Beng", helloWord);
        assertEquals("I'm so lonely", imSoLonely);
        assertEquals("accountNo 62142_mock", accountNo);
        assertNull(invalidPath);
    }
}