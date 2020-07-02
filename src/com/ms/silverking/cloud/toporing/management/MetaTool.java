package com.ms.silverking.cloud.toporing.management;

import java.io.IOException;

import org.apache.zookeeper.KeeperException;

import com.ms.silverking.cloud.management.MetaToolBase;
import com.ms.silverking.cloud.management.MetaToolModule;
import com.ms.silverking.cloud.management.MetaToolOptions;
import com.ms.silverking.cloud.meta.CloudConfiguration;
import com.ms.silverking.cloud.meta.VersionedDefinition;
import com.ms.silverking.cloud.toporing.meta.MetaClient;
import com.ms.silverking.cloud.toporing.meta.NamedRingConfiguration;
import com.ms.silverking.cloud.toporing.meta.RingConfiguration;
import com.ms.silverking.cloud.toporing.meta.RingConfigurationZK;
import com.ms.silverking.cloud.toporing.meta.StoragePolicyGroupZK;
import com.ms.silverking.cloud.toporing.meta.WeightsZK;
import com.ms.silverking.cloud.zookeeper.ZooKeeperConfig;

public class MetaTool extends MetaToolBase {
  private enum Tool {Weights, RingConfiguration, StoragePolicyGroup}

  ;

  public MetaTool() {
  }

  private static MetaToolModule getModule(Tool tool, MetaClient metaClient) throws KeeperException {
    switch (tool) {
    case Weights:
      return new WeightsZK(metaClient);
    //case Replication: return new ReplicationZK(metaClient);
    case RingConfiguration:
      return new RingConfigurationZK(metaClient);
    case StoragePolicyGroup:
      return new StoragePolicyGroupZK(metaClient);
    default:
      throw new RuntimeException("panic");
    }
  }

  private static NamedRingConfiguration namedRingConfigurationFor(Tool tool, String name) {
    switch (tool) {
    case Weights:
      return new NamedRingConfiguration(null,
          new RingConfiguration(CloudConfiguration.emptyTemplate, name, null, null, null, null,
              VersionedDefinition.NO_VERSION));
    //case Replication: return new NamedRingConfiguration(null,
    //        new RingConfiguration(new CloudConfiguration(null, null), null, name, null, VersionedDefinition
    //        .NO_VERSION));
    case RingConfiguration:
      return new NamedRingConfiguration(name,
          new RingConfiguration(CloudConfiguration.emptyTemplate, null, null, null, null, null,
              VersionedDefinition.NO_VERSION));
    case StoragePolicyGroup:
      return new NamedRingConfiguration(null,
          new RingConfiguration(CloudConfiguration.emptyTemplate, null, null, name, null, null,
              VersionedDefinition.NO_VERSION));
    default:
      throw new RuntimeException("panic");
    }
  }

  @Override
  protected void doWork(MetaToolOptions options) throws IOException, KeeperException {
    MetaClient mc;
    Tool tool;

    tool = Tool.valueOf(options.tool);
    mc = new MetaClient(namedRingConfigurationFor(tool, options.name), new ZooKeeperConfig(options.zkConfig));
    doWork(options, new MetaToolWorker(getModule(tool, mc)));
  }

  public static void main(String[] args) {
    new MetaTool().runTool(args);
  }
}
