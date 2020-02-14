import jenkins.model.*;
import org.jenkinsci.plugins.workflow.libs.*;
import jenkins.scm.api.SCMSource;
import jenkins.plugins.git.*;
import com.cloudbees.pipeline.governance.templates.*;
import com.cloudbees.pipeline.governance.templates.catalog.*;
import java.util.logging.Logger;

Logger logger = Logger.getLogger("createPipelineTemplateCatalog.groovy");

SCMSource scm = new GitSCMSource("https://github.com/cb-demos/core-pipeline-template-catalog.git");
scm.setCredentialsId("github-token");

TemplateCatalog catalog = new TemplateCatalog(scm, "master");
catalog.setUpdateInterval("1h");

GlobalTemplateCatalogManagement.get().addCatalog(catalog);
GlobalTemplateCatalogManagement.get().save();
logger.info("Creating new Pipeline Template Catalog");

catalog.updateFromSCM(); 
