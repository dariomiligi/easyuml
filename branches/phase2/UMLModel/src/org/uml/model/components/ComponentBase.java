package org.uml.model.components;

import org.uml.model.members.*;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import org.uml.model.ClassDiagram;
import org.uml.model.ContainerBase;
import org.uml.model.Visibility;
import org.uml.model.relations.RelationBase;

/**
 * Base class for all UML class diagram components (classes, interfaces or
 * enums) that can be added to class diagram. Note that relations are not
 * represented with this class as well; they are represented with
 * RelationComponent.
 *
 * @author zoran
 * @see ClassComponent
 * @see RelationBase
 */
public abstract class ComponentBase extends ContainerBase<MemberBase> implements Serializable {

    private transient ClassDiagram parentDiagram;

    private String parentPackage;
//    private PackageComponent parentPackage;
    private Visibility visibility;

    private Point position; // this should be removed in future
    private Rectangle bounds;

    /**
     * Default constructor. Initializes members of the ComponentBase. Members
     * can be fields, methods, constructors and literals.
     *
     * @param name of component
     * @see Field
     * @see Method
     * @see Constructor
     * @see Literal
     */
    public ComponentBase(String name) {
        super(name);
        parentPackage = "default";
        visibility = Visibility.PUBLIC;
    }

    public abstract void removeMemberFromContainer(MemberBase member);

    /**
     * Returns members that this ClassDiagramComponent has
     *
     * @return all members of this ClassDiagramComponent
     */
    public LinkedHashSet<MemberBase> getMembers() {
        return new LinkedHashSet(containerComponents);
    }

    /**
     * Returns parentDiagram of this ClassDiagramComponent. Parent diagram is a
     * ClassDiagram object that contains this ClassDiagramComponent.
     *
     * @return ClassDiagram containing this ClassDiagramComponent
     * @see ClassDiagram
     */
    public ClassDiagram getParentDiagram() {
        return parentDiagram;
    }

    /**
     * Sets parentDiagram of this ClassDiagramComponent. Parent diagram is a
     * ClassDiagram object that contains this ClassDiagramComponent.
     *
     * @param parentDiagram
     * @see ClassDiagram
     */
    public void setParentDiagram(ClassDiagram parentDiagram) {
        this.parentDiagram = parentDiagram;
    }

    /**
     * Returns position that this ClassDiagramComponent has on the scene
     *
     * @return point that represents X and Y coordinates
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets this ClassDiagramComponent's position on scene
     *
     * @param position that the component has
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    public String getParentPackage() {
        return parentPackage;
    }

    public void setParentPackage(String parentPackage) {        
        String oldParentPackage = this.parentPackage;
        this.parentPackage = parentPackage;
        fire("parentPackage", oldParentPackage, parentPackage);
    }
    
    @Override
    public String getSignature(){
        return getParentPackage() + " " + getName();
    }
    
    public String deriveNewSignatureFromName(String newName){
        return getParentPackage() + " " + newName;
    }
    
    public String deriveNewSignatureFromPackage(String newParentPackage){
        return newParentPackage + " " + getName();
    }
    
//    /**
//     * Returns package that contains this ClassDiagramComponent
//     *
//     * @return PackageComponent of this ClassDiagramComponent
//     * @see PackageComponent
//     */
//    public PackageComponent getParentPackage() {
//        return parentPackage;
//    }
//
//    /**
//     * Sets the package that contains this ClassDiagramComponent
//     *
//     * @param parentPackage
//     */
//    public void setParentPackage(PackageComponent parentPackage) {
//        this.parentPackage = parentPackage;
//        //   parentPackage.addMember(this);
//    }

    /**
     * Returns the visibility modifier of this ClassDiagramComponent
     *
     * @return visibility of this ClassDiagramComponent
     * @see Visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility modifier of this ClassDiagramComponent
     *
     * @param visibility to be set
     * @see Visibility
     */
    public void setVisibility(Visibility visibility) {
        Visibility oldVisibility = this.visibility;
        this.visibility = visibility;
        fire("visibility", oldVisibility, visibility);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    @Override
    public String toString() {
        return getSignature();
    }   

}
