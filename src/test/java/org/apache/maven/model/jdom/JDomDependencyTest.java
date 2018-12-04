package org.apache.maven.model.jdom;

/*
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
 */

import static org.apache.maven.model.jdom.util.JDomUtils.getChildElementTextTrim;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringReader;

import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.junit.Test;

public class JDomDependencyTest
{
    private SAXBuilder builder = new SAXBuilder();

    @Test( expected = UnsupportedOperationException.class )
    public void testIsOptional()
    {
        new JDomDependency( null ).isOptional();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSetOptional()
    {
        new JDomDependency( null ).setOptional( true );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testAddExclusion()
    {
        new JDomDependency( null ).addExclusion( null );
    }

    @Test
    public void testGetArtifactId() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getArtifactId() );

        content = "<dependency><artifactId>ARTIFACTID</artifactId></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "ARTIFACTID", new JDomDependency( dependencyElm ).getArtifactId() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testGetClassifier()
    {
        new JDomDependency( null ).getClassifier();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testGetExclusions()
    {
        new JDomDependency( null ).getExclusions();
    }

    @Test
    public void testGetGroupId() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getGroupId() );

        content = "<dependency><groupId>GROUPID</groupId></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "GROUPID", new JDomDependency( dependencyElm ).getGroupId() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testGetScope()
    {
        new JDomDependency( null ).getScope();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testGetSystemPath()
    {
        new JDomDependency( null ).getSystemPath();
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testGetType()
    {
        new JDomDependency( null ).getType();
    }

    @Test
    public void testGetVersion() throws Exception
    {
        String content = "<dependency></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertNull( new JDomDependency( dependencyElm ).getVersion() );

        content = "<dependency><version>VERSION</version></dependency>";
        dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        assertEquals( "VERSION", new JDomDependency( dependencyElm ).getVersion() );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testRemoveExclusion()
    {
        new JDomDependency( null ).removeExclusion( null );;
    }

    @Test
    public void testSetArtifactIdString() throws Exception
    {
        String content = "<dependency><artifactId>OLD_ARTIFACTID</artifactId></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setArtifactId( "NEW_ARTIFACTID" );
        assertEquals( "NEW_ARTIFACTID", getChildElementTextTrim( "artifactId", dependencyElm ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSetClassifierString()
    {
        new JDomDependency( null ).setClassifier( null );;
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSetExclusions()
    {
        new JDomDependency( null ).setExclusions( null );;
    }

    @Test
    public void testSetGroupIdString() throws Exception
    {
        String content = "<dependency><groupId>OLD_GROUPID</groupId></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setGroupId( "NEW_GROUPID" );
        assertEquals( "NEW_GROUPID", getChildElementTextTrim( "groupId", dependencyElm ) );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSetScopeString()
    {
        new JDomDependency( null ).setScope( null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSetSystemPathString()
    {
        new JDomDependency( null ).setSystemPath( null );
    }

    @Test( expected = UnsupportedOperationException.class )
    public void testSetTypeString()
    {
        new JDomDependency( null ).setType( null );
    }

    @Test
    public void testSetVersionString() throws Exception
    {
        String content = "<dependency><version>OLD_VERSION</version></dependency>";
        Element dependencyElm = builder.build( new StringReader( content ) ).getRootElement();
        new JDomDependency( dependencyElm ).setVersion( "NEW_VERSION" );
        assertEquals( "NEW_VERSION", getChildElementTextTrim( "version", dependencyElm ) );
    }

    @Test
    public void testGetName()
    {
        assertEquals( "dependency", new JDomDependency( null ).getName() );
    }
}
