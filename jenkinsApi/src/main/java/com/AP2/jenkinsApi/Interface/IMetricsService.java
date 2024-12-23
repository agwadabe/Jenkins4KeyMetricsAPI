package com.AP2.jenkinsApi.Interface;

public interface IMetricsService {

    int calculateDeploymentFrequency(String jobName) throws Exception;

    long calculateLeadTimeForChanges(String jobName) throws Exception;

    double calculateChangeFailureRate(String jobName) throws Exception;

    double calculateTimeToRestoreService(String jobName) throws Exception;
}
