package com.hcl.devops.connect.Endpoints;

public interface IEndpoints {

    public String getSyncApiEndpoint();

    public String getPipelinesEndpoint();

    public String getSyncApiEndpoint(String baseUrl);

    public String getSyncStoreEndpoint();

    public String getConnectEndpoint();

    public String getQualityDataEndpoint();

    public String getQualityDataRawEndpoint();

    public String getAccelerateHostname();

    public String getReleaseEvensApiEndpoint();

    public String getDotsEndpoint();
}