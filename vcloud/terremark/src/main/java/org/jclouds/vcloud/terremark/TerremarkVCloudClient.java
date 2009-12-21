/**
 *
 * Copyright (C) 2009 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.vcloud.terremark;

import java.net.InetAddress;
import java.util.SortedSet;
import java.util.concurrent.TimeUnit;

import org.jclouds.concurrent.Timeout;
import org.jclouds.vcloud.VCloudClient;
import org.jclouds.vcloud.options.InstantiateVAppTemplateOptions;
import org.jclouds.vcloud.terremark.domain.ComputeOptions;
import org.jclouds.vcloud.terremark.domain.CustomizationParameters;
import org.jclouds.vcloud.terremark.domain.InternetService;
import org.jclouds.vcloud.terremark.domain.InternetServiceConfiguration;
import org.jclouds.vcloud.terremark.domain.IpAddress;
import org.jclouds.vcloud.terremark.domain.Node;
import org.jclouds.vcloud.terremark.domain.NodeConfiguration;
import org.jclouds.vcloud.terremark.domain.Protocol;
import org.jclouds.vcloud.terremark.domain.PublicIpAddress;
import org.jclouds.vcloud.terremark.domain.TerremarkVApp;
import org.jclouds.vcloud.terremark.options.AddInternetServiceOptions;
import org.jclouds.vcloud.terremark.options.AddNodeOptions;

/**
 * Provides access to VCloud resources via their REST API.
 * <p/>
 * 
 * @see <a href="https://community.vcloudexpress.terremark.com/en-us/discussion_forums/f/60.aspx" />
 * @author Adrian Cole
 */
@Timeout(duration = 45, timeUnit = TimeUnit.SECONDS)
public interface TerremarkVCloudClient extends VCloudClient {

   @Override
   TerremarkVApp instantiateVAppTemplateInVDC(String vDCId, String appName, String templateId,
            InstantiateVAppTemplateOptions... options);

   @Override
   TerremarkVApp getVApp(String vAppId);

   /**
    * This call returns the compute options for the vApp. The compute options are the CPU and memory
    * configurations supported by Terremark and by the guest operating system of the vApp. This call
    * also returns the cost per hour for each configuration.
    */
   SortedSet<ComputeOptions> getComputeOptionsOfVApp(String vAppId);

   SortedSet<ComputeOptions> getComputeOptionsOfCatalogItem(String catalogItemId);

   /**
    * This call returns the customization options for the vApp. The response lists which
    * customization options are supported for this particular vApp. The possible customization
    * options are Network and Password.
    */
   CustomizationParameters getCustomizationOptionsOfVApp(String vAppId);

   CustomizationParameters getCustomizationOptionsOfCatalogItem(String catalogItemId);

   /**
    * This call returns a list of public IP addresses.
    */
   SortedSet<PublicIpAddress> getPublicIpsAssociatedWithVDC(String vDCId);

   void deletePublicIp(int ipId);

   /**
    * The call creates a new internet server, including protocol and port information. The public IP
    * is dynamically allocated.
    * 
    */
   InternetService addInternetServiceToVDC(String vDCId, String serviceName, Protocol protocol,
            int port, AddInternetServiceOptions... options);

   /**
    * This call adds an internet service to a known, existing public IP. This call is identical to
    * Add Internet Service except you specify the public IP in the request.
    * 
    */
   InternetService addInternetServiceToExistingIp(int existingIpId, String serviceName,
            Protocol protocol, int port, AddInternetServiceOptions... options);

   void deleteInternetService(int internetServiceId);

   InternetService getInternetService(int internetServiceId);

   InternetService configureInternetService(int internetServiceId,
            InternetServiceConfiguration nodeConfiguration);

   SortedSet<InternetService> getAllInternetServicesInVDC(String vDCId);

   /**
    * This call returns information about the internet service on a public IP.
    */
   SortedSet<InternetService> getInternetServicesOnPublicIp(int ipId);

   SortedSet<InternetService> getPublicIp(int ipId);

   /**
    * This call adds a node to an existing internet service.
    * <p/>
    * Every vDC is assigned a network of 60 IP addresses that can be used as nodes. Each node can
    * associated with multiple internet service. You can get a list of the available IP addresses by
    * calling Get IP Addresses for a Network.
    * 
    * @param internetServiceId
    * @param ipAddress
    * @param name
    * @param port
    * @param options
    * @return
    */
   Node addNode(int internetServiceId, InetAddress ipAddress, String name, int port,
            AddNodeOptions... options);

   Node getNode(int nodeId);

   Node configureNode(int nodeId, NodeConfiguration nodeConfiguration);

   void deleteNode(int nodeId);

   SortedSet<Node> getNodes(int internetServiceId);

   SortedSet<IpAddress> getIpAddressesForNetwork(String networkId);

}
