package org.apache.maven.model.jdom;

/*
 * Copyright 2018 CoreMedia AG, Hamburg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.apache.maven.model.jdom.util.JDomUtils.detectIndentation;
import static org.apache.maven.model.jdom.util.JDomUtils.insertNewElement;
import static org.apache.maven.model.jdom.util.JDomUtils.removeChildElement;
import static org.apache.maven.model.jdom.util.JDomUtils.resetIndentations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;

import org.apache.maven.model.Activation;
import org.apache.maven.model.BuildBase;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.DependencyManagement;
import org.apache.maven.model.DistributionManagement;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Reporting;
import org.apache.maven.model.Repository;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.ElementFilter;

public class JDomProfiles extends ArrayList<Profile>
{
    private Element profiles;
    private JDomModel parent;

    public JDomProfiles( Element profiles, JDomModel parent )
    {
        super( transformToJDomProfileList( getProfileElements( profiles ) ) );
        this.profiles = profiles;
        this.parent = parent;
    }

    private static List<Element> getProfileElements( Element profiles )
    {
        if ( profiles == null )
        {
            return emptyList();
        }
        else
        {
            return profiles.getContent( new ElementFilter( "profile", profiles.getNamespace() ) );
        }
    }

    private static List<JDomProfile> transformToJDomProfileList( List<Element> profileElements )
    {
        List<JDomProfile> jDomProfileList = new ArrayList<>( profileElements.size() );
        for ( Element profileElement : profileElements )
        {
            jDomProfileList.add( new JDomProfile( profileElement ) );
        }
        return jDomProfileList;
    }

    @Override
    public boolean add( Profile profile )
    {
        if ( profiles == null )
        {
            profiles = insertNewElement( "profiles", parent.getJDomElement() );
        }

        Element newElement;
        if ( profile instanceof JDomProfile )
        {
            newElement = ( (JDomProfile) profile ).getJDomElement().clone();
            profiles.addContent(
                profiles.getContentSize() - 1,
                asList(
                    new Text( "\n" + detectIndentation( profiles ) ),
                    newElement ) );
            resetIndentations( profiles, detectIndentation( profiles ) );
            resetIndentations( newElement, detectIndentation( profiles ) + "  " );
        }
        else
        {
            newElement = insertNewElement( "profile", profiles );
            JDomProfile jDomProfile = new JDomProfile( newElement );
            jDomProfile.setId( profile.getId() );

            Activation activation = profile.getActivation();
            if ( activation != null )
            {
                jDomProfile.setActivation( activation );
            }

            BuildBase build = profile.getBuild();
            if ( build != null )
            {
                jDomProfile.setBuild( build );
            }

            List<Dependency> dependencies = profile.getDependencies();
            if ( !dependencies.isEmpty() )
            {
                jDomProfile.setDependencies( dependencies );
            }

            DependencyManagement dependencyManagement = profile.getDependencyManagement();
            if ( dependencyManagement != null )
            {
                jDomProfile.setDependencyManagement( dependencyManagement );
            }

            DistributionManagement distributionManagement = profile.getDistributionManagement();
            if ( distributionManagement != null )
            {
                jDomProfile.setDistributionManagement( distributionManagement );
            }

            List<String> modules = profile.getModules();
            if ( !modules.isEmpty() )
            {
                jDomProfile.setModules( modules );
            }

            List<Repository> pluginRepositories = profile.getPluginRepositories();
            if ( !pluginRepositories.isEmpty() )
            {
                jDomProfile.setPluginRepositories( pluginRepositories );
            }

            List<Repository> repositories = profile.getRepositories();
            if ( !repositories.isEmpty() )
            {
                jDomProfile.setRepositories( repositories );
            }

            Properties properties = profile.getProperties();
            if ( !properties.isEmpty() )
            {
                jDomProfile.setProperties( properties );
            }

            Reporting reporting = profile.getReporting();
            if ( reporting != null )
            {
                jDomProfile.setReporting( reporting );
            }
        }

        return super.add( profile );
    }

    @Override
    public boolean remove( final Object profile )
    {
        Profile removeProfile = (Profile) profile;
        for ( Profile candidate : this )
        {
            if ( candidate.getId().equals( removeProfile.getId() ) )
            {
                removeChildElement( profiles, ( (JDomProfile) candidate ).getJDomElement() );
                boolean remove = super.remove( candidate );
                if ( super.isEmpty() )
                {
                    removeChildElement( parent.getJDomElement(), profiles );
                }
                return remove;
            }
        }
        return false;
    }

    @Override
    public boolean addAll( Collection<? extends Profile> profiles )
    {
        boolean added = false;
        for ( Profile profile : profiles )
        {
            added |= this.add( profile );
        }
        return added;
    }

    @Override
    public boolean addAll( int index, Collection<? extends Profile> profiles )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll( Collection<?> profiles )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll( Collection<?> profiles )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear()
    {
        while ( size() > 0 )
        {
            remove( 0 );
        }
    }

    @Override
    public Profile set( int index, Profile profile )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add( int index, Profile profile )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public Profile remove( int index )
    {
        Profile profile = get( index );
        remove( profile );
        return profile;
    }

    @Override
    public int lastIndexOf( Object profile )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Profile> listIterator()
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Profile> listIterator( int index )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Profile> subList( int fromIndex, int toIndex )
    {
        throw new UnsupportedOperationException();
    }
}