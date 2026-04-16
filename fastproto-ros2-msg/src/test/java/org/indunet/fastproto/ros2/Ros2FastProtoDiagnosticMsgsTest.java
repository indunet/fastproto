package org.indunet.fastproto.ros2;

import org.indunet.fastproto.ros2.builtin_interfaces.msg.Time;
import org.indunet.fastproto.ros2.diagnostic_msgs.msg.DiagnosticArray;
import org.indunet.fastproto.ros2.diagnostic_msgs.msg.DiagnosticStatus;
import org.indunet.fastproto.ros2.diagnostic_msgs.msg.KeyValue;
import org.indunet.fastproto.ros2.std_msgs.msg.Header;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Ros2FastProtoDiagnosticMsgsTest {
    @Test
    void testDiagnosticArrayRoundTripWithMessageHelpers() {
        DiagnosticArray diagnosticArray = DiagnosticArray.builder()
                .header(Header.builder()
                        .stamp(Time.builder().sec(12).nanosec(34).build())
                        .frameId("diagnostics")
                        .build())
                .status(new DiagnosticStatus[]{
                        DiagnosticStatus.builder()
                                .level(DiagnosticStatus.WARN)
                                .name("motor_controller")
                                .message("temperature high")
                                .hardwareId("mc-01")
                                .values(new KeyValue[]{
                                        KeyValue.builder().key("temp_c").value("82.5").build(),
                                        KeyValue.builder().key("fan").value("on").build()
                                })
                                .build(),
                        DiagnosticStatus.builder()
                                .level(DiagnosticStatus.OK)
                                .name("battery")
                                .message("healthy")
                                .hardwareId("bat-01")
                                .values(new KeyValue[]{
                                        KeyValue.builder().key("soc").value("0.78").build()
                                })
                                .build()
                })
                .build();

        assertEquals(diagnosticArray, DiagnosticArray.decode(diagnosticArray.encode()));
    }
}
