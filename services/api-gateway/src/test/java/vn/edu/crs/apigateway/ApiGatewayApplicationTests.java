package vn.edu.crs.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class ApiGatewayApplicationTests {
    @Autowired
    private RouteLocator routeLocator;

    @Test
    void configuresAllPublicRoutes() {
        assertThat(routeLocator.getRoutes().collectList().block())
                .extracting(route -> route.getId())
                .containsExactlyInAnyOrder(
                        "auth-service",
                        "course-service-list",
                        "course-service-detail",
                        "registration-service-list",
                        "registration-service-detail",
                        "course-service-partner");
    }
}
