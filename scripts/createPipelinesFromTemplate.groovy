import hudson.model.*;
import jenkins.model.*;
import com.cloudbees.hudson.plugins.folder.*;
import com.cloudbees.opscenter.bluesteel.folder.*;
import java.util.logging.Logger;

Logger logger = Logger.getLogger("createPipelinesFromTemplate.groovy");

def backendConfig = """
<org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject plugin="workflow-multibranch@2.21">
  <properties>
    <com.cloudbees.pipeline.governance.templates.classic.multibranch.GovernanceMultibranchPipelinePropertyImpl plugin="cloudbees-workflow-template@3.3">
      <instance>
        <model>workshopCatalog/python</model>
        <values class="tree-map">
          <entry>
            <string>githubCredentialId</string>
            <string>github-token</string>
          </entry>
          <entry>
            <string>name</string>
            <string>microblog-backend</string>
          </entry>
          <entry>
            <string>registry</string>
            <string>docker.cb-demos.io</string>
          </entry>
          <entry>
            <string>repoOwner</string>
            <string>cb-demos</string>
          </entry>
          <entry>
            <string>repository</string>
            <string>microblog-backend</string>
          </entry>
        </values>
      </instance>
    </com.cloudbees.pipeline.governance.templates.classic.multibranch.GovernanceMultibranchPipelinePropertyImpl>
  </properties>
</org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject>
""";

def frontendConfig = """
<org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject plugin="workflow-multibranch@2.21">
  <actions />
  <properties>
    <com.cloudbees.pipeline.governance.templates.classic.multibranch.GovernanceMultibranchPipelinePropertyImpl plugin="cloudbees-workflow-template@3.3">
      <instance>
        <model>workshopCatalog/vuejs-app</model>
        <values class="tree-map">
          <entry>
            <string>gcpProject</string>
            <string>core-flow-research</string>
          </entry>
          <entry>
            <string>githubCredentialId</string>
            <string>github-token</string>
          </entry>
          <entry>
            <string>name</string>
            <string>microblog-frontend</string>
          </entry>
          <entry>
            <string>registry</string>
            <string>docker.cb-demos.io</string>
          </entry>
          <entry>
            <string>repoOwner</string>
            <string>cb-demos</string>
          </entry>
          <entry>
            <string>repository</string>
            <string>microblog-frontend</string>
          </entry>
        </values>
      </instance>
    </com.cloudbees.pipeline.governance.templates.classic.multibranch.GovernanceMultibranchPipelinePropertyImpl>
  </properties>
</org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject>
""";

def jobMap = [
  'microblog-backend': backendConfig, // "https://raw.githubusercontext.com/cb-demos/core-mm-config-bundles/master/jobs/microblog-backend/config.xml"
  'microblog-frontend': frontendConfig // "https://raw.githubusercontext.com/cb-demos/core-mm-config-bundles/master/jobs/microblog-frontend/config.xml"
];

def teamFolder = Jenkins.instance.getAllItems(BlueSteelTeamFolder).find{it.name.equals("alpha")};

jobMap.each{ name, configXml ->  // configUrl
  def job = teamFolder.getItem(name);
  if (job != null) {
    logger.info("Job $name already exists - skipping");
  } else {
    logger.info("Creating $name from Pipeline Template");
    // def configXml = new URL(configUrl).getText();
    def p = teamFolder.createProjectFromXML(name, new ByteArrayInputStream(configXml.getBytes("UTF-8")));
  }
};