package org.apache.maven.model.jdom.it;

import static org.apache.maven.model.jdom.etl.ModelETLRequest.UNIX_LS;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.maven.model.jdom.etl.JDomModelETL;
import org.apache.maven.model.jdom.etl.JDomModelETLFactory;
import org.apache.maven.model.jdom.etl.ModelETLRequest;
import org.codehaus.plexus.util.FileUtils;
import org.jdom2.JDOMException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.rules.TestName;

/**
 * This abstract class offers the base implementation that allows to add tests consisting of the transformation code, an
 * <u>*_input-pom.xml</u> containing the input XML file and a <u>*_expected-pom.xml</u> file containing the expected XML
 * after transformation. The file names must follow the pattern {@code [TEST_CLASS]_[TEST_METHOD]_[input|expected]-pom.xml}.
 * The transformation output is written to a file {@code [TEST_CLASS]_[TEST_METHOD]_output-pom.xml}. It is placed into a
 * folder that can be configured using the system property the {@code test.output.directory}. If that property is not
 * set the output will be written to a temporary file that is deleted after the test.
 *
 * @author Marc Rohlfs, CoreMedia AG
 */
public abstract class AbstractJDomModelEtlIT
{
    @Rule
    public TestName testName = new TestName();

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @SuppressWarnings( "WeakerAccess" )
    protected JDomModelETL jDomModelETL;

    private File expectedPomFile;
    private File outputPomFile;

    @Before
    public void setUp() throws IOException, JDOMException, URISyntaxException
    {
        String filenamePrefix = this.getClass().getSimpleName() + "_" + testName.getMethodName();
        File inputPomFile = getTestResource( filenamePrefix + "_input-pom.xml" );
        expectedPomFile = getTestResource( filenamePrefix + "_expected-pom.xml" );
        outputPomFile = getOutputFile( filenamePrefix + "_output-pom.xml" );

        final ModelETLRequest modelETLRequest = new ModelETLRequest();
        modelETLRequest.setLineSeparator( UNIX_LS );
        jDomModelETL = new JDomModelETLFactory().newInstance( modelETLRequest );
        jDomModelETL.extract( inputPomFile );
    }

    @SuppressWarnings( "WeakerAccess" )
    protected void assertTransformation() throws IOException
    {
        jDomModelETL.load( outputPomFile );

        String actualXml = FileUtils.fileRead( outputPomFile );
        String expectedXml = FileUtils.fileRead( expectedPomFile );
        String message = "Unexpected contents in output file " + outputPomFile + System.getProperty( "line.separator" );
        assertEquals( message, expectedXml, actualXml );
    }

    private File getOutputFile( String filename ) throws IOException
    {
        String outputDirectory = ( System.getProperty( "test.output.directory" ) );
        if ( outputDirectory == null )
        {
            // Write the output to a tmp file - applies when tests are executed in the IDE.
            return folder.newFile( filename );
        }
        else
        {
            // Write the output to a file in the output dir - applies when tests are executed by the Maven build.
            File outputDir = new File( outputDirectory );
            outputDir.mkdirs();
            return new File( outputDirectory, this.getClass().getPackage().getName() + "." + filename );
        }

    }

    private File getTestResource( String filename ) throws FileNotFoundException, URISyntaxException
    {
        URL resource = this.getClass().getResource( filename );
        if ( resource == null )
        {
            throw new FileNotFoundException( "Test resource not found: " + filename );
        }
        else
        {
            return new File( resource.toURI() );
        }
    }
}