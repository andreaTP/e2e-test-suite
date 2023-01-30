package io.managed.services.test.client.kafkamgmt;

import com.microsoft.kiota.ApiException;
import com.openshift.cloud.api.kas.ApiClient;
import com.openshift.cloud.api.kas.api.kafkas_mgmt.v1.kafkas.item.metrics.query.MetricsInstantQueryListResponse;
import com.openshift.cloud.api.kas.models.KafkaRequest;
import com.openshift.cloud.api.kas.models.KafkaRequestList;
import com.openshift.cloud.api.kas.models.KafkaRequestPayload;
import com.openshift.cloud.api.kas.models.KafkaUpdateRequest;
import io.managed.services.test.client.BaseApi;
import io.managed.services.test.client.exception.ApiGenericException;
import io.managed.services.test.client.exception.ApiUnknownException;
import io.managed.services.test.client.oauth.KeycloakLoginSession;
import io.managed.services.test.client.oauth.KeycloakUser;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Log4j2
public class KafkaMgmtApi extends BaseApi {

    private final ApiClient apiClient;

    public KafkaMgmtApi(ApiClient apiClient) {
        super();
        this.apiClient = Objects.requireNonNull(apiClient);
    }

    @Override
    protected ApiUnknownException toApiException(Exception e) {
        if (e instanceof ApiException) {
            var ex = (ApiException) e;
            return new ApiUnknownException(ex.getMessage(), ex);
        }
        return null;
    }

    @Override
    protected void setAccessToken(String t) {
        // no op
        // ((HttpBearerAuth) apiClient.getAuthentication("Bearer")).setBearerToken(t);
    }

    public KafkaRequest getKafkaById(String id) throws ApiGenericException {
        return retry(() -> apiClient.api().kafkas_mgmt().v1().kafkas(id).get().get(1, TimeUnit.SECONDS));
    }

    public KafkaRequestList getKafkas(String page, String size, String orderBy, String search) throws ApiGenericException {
        return retry(() -> apiClient.api().kafkas_mgmt().v1().kafkas().get(config -> {
            config.queryParameters.page = page;
            config.queryParameters.size = size;
            config.queryParameters.orderBy = orderBy;
            config.queryParameters.search = search;
        }).get(1, TimeUnit.SECONDS));
    }

    public KafkaRequest createKafka(Boolean async, KafkaRequestPayload kafkaRequestPayload) throws ApiGenericException {
        return retry(() -> apiClient.api().kafkas_mgmt().v1().kafkas().post(kafkaRequestPayload).get(1, TimeUnit.SECONDS));
    }

    public void deleteKafkaById(String id, Boolean async) throws ApiGenericException {
        // TODO: why does it return Error
        retry(() -> apiClient.api().kafkas_mgmt().v1().kafkas(id).delete(config -> config.queryParameters.async = async));
    }

    public MetricsInstantQueryListResponse getMetricsByInstantQuery(String id, List<String> filters) throws ApiGenericException {
        return retry(() -> apiClient.api().kafkas_mgmt().v1().kafkas(id).metrics().query().get(config -> config.queryParameters.filters = filters.toArray(new String[0])).get(1, TimeUnit.SECONDS));
    }

    public String federateMetrics(String id) throws ApiGenericException {
        return retry(() -> apiClient.api().kafkas_mgmt().v1().kafkas(id).metrics().federate().get().get(1, TimeUnit.SECONDS));
    }

    public KafkaRequest updateKafka(String id, KafkaUpdateRequest kafkaUpdateRequest) throws ApiGenericException {
        return retry(() -> apiClient.api().kafkas_mgmt().v1().kafkas(id).patch(kafkaUpdateRequest).get(1, TimeUnit.SECONDS));
    }
}
