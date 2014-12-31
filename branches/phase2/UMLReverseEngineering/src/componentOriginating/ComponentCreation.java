package componentOriginating;

import javax.lang.model.element.Element;
import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.FIELD;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Literal;
import org.uml.model.members.Method;
import org.uml.reveng.unused.CompilationProcessor;
import org.uml.reveng.unused.GeneratedDiagramManager;

/**
 * Classes, Enums and Interfaces found during compilation process are being
 * created, populated and stored for later handling inside adjacent collections,
 * by this class.
 *
 * @author Milan
 */
public class ComponentCreation {

    /**
     * Takes an Element object from the Compilation processor and creates an
     * object similar to it, that is used in diagram creation. Here are Classes,
     * Enums and Interfaces created.
     *
     * @param tlcc element to be processed
     * @see Element
     * @see CompilationProcessor
     */
    public static void createComponent(Element tlcc) {
        Object[] rootModifiers = tlcc.getModifiers().toArray();
        switch (tlcc.getKind()) {
            case CLASS:
                CompilationProcessor.genClass = classBuilder(tlcc, rootModifiers);
                populateComponent(tlcc);
//                CompilationProcessor.allFoundClasses.add(CompilationProcessor.genClass);
                try {
                    CompilationProcessor.generatedDiagram.addPartToContainter(CompilationProcessor.genClass);
                } catch (RuntimeException rtex) {
                    System.out.println("Error in Class creation!");
                }
                break;
            case ENUM:
                CompilationProcessor.genEnum = enumBuilder(tlcc, rootModifiers);
//                CompilationProcessor.allFoundClasses.add(CompilationProcessor.genEnum);
                try {
                    CompilationProcessor.generatedDiagram.addPartToContainter(CompilationProcessor.genEnum);
                } catch (RuntimeException rtex) {
                    System.out.println("Error in Enum creation!");
                }
                break;
            case INTERFACE:
                CompilationProcessor.genInterface = interfaceBuilder(tlcc, rootModifiers);
//                CompilationProcessor.allFoundClasses.add(CompilationProcessor.genInterface);
                try {
                    CompilationProcessor.generatedDiagram.addPartToContainter(CompilationProcessor.genInterface);
                } catch (RuntimeException rtex) {
                    System.out.println("Error in Interface creation!");
                }
                break;
        }
    }

    /**
     * Creates a Class component object based upon given Element object.
     *
     * @param elem based on which a Class component should be created
     * @param modifierElemnts that define the behavior of this class
     * (abstract...)
     * @return fully created Class component
     * @see ClassComponent
     */
    private static ClassComponent classBuilder(Element elem, Object[] modifierElemnts) {
//        String projectPath = elem.toString();
        String className = elem.getSimpleName().toString();
//        String packages = projectPath.split(className)[0].substring(0, projectPath.length() - className.length() - 1);
        ClassComponent createdClass = new ClassComponent();
        createdClass.setName(className);
        MemberBuilding.setModifiers(createdClass, modifierElemnts);
//        MemberBuilding.packageSelector(createdClass, packages);        
        createdClass.setLocation(GeneratedDiagramManager.getInstance().getNextComponentPosition());
        return createdClass;
    }

    /**
     * Creates an Interface component object based upon given Element object.
     *
     * @param elem based on which an Interface component should be created
     * @param modifierElemnts that define the behavior of this interface
     * (abstract...)
     * @return fully created Interface component
     * @see InterfaceComponent
     */
    private static InterfaceComponent interfaceBuilder(Element elem, Object[] modifierElemnts) {
        String projectPath = elem.toString();
        InterfaceComponent createdInterface = new InterfaceComponent();
        String interfaceName = elem.getSimpleName().toString();
//        String packages = projectPath.split(interfaceName)[0].substring(0, projectPath.length() - interfaceName.length() - 1);
        createdInterface.setName(interfaceName);
        MemberBuilding.setModifiers(elem, modifierElemnts);
//        MemberBuilding.packageSelector(createdInterface, packages);
        createdInterface.setLocation(GeneratedDiagramManager.getInstance().getNextComponentPosition());
        return createdInterface;
    }

    /**
     * Creates an Enum component object based upon given Element object.
     *
     * @param elem based on which an Enum component should be created
     * @param modifierElemnts that define the behavior of this enum
     * (abstract...)
     * @return fully created Enum component
     * @see EnumComponent
     */
    private static EnumComponent enumBuilder(Element elem, Object[] modifierElemnts) {
        String projectPath = elem.toString();
        String enumName = elem.getSimpleName().toString();
//        String packages = projectPath.split(enumName)[0].substring(0, projectPath.length() - enumName.length() - 1);
        EnumComponent createdEnum = new EnumComponent();
        createdEnum.setName(enumName);
        MemberBuilding.setModifiers(elem, modifierElemnts);
//        MemberBuilding.packageSelector(createdEnum, packages);
        createdEnum.setLocation(GeneratedDiagramManager.getInstance().getNextComponentPosition());
        return createdEnum;
    }

    /**
     * Populates Class, Enum or Interface by adding them Fields, Methods,
     * Constructors (and Literals) that the Element object, based upon which
     * they are created, contains. Classes, Enums and Interfaces that are being
     * modified are ones inside the CompilationProcesseor.
     *
     * @param tlc element based on which the component is created
     */
    public static void populateComponent(Element tlc) {
        int methodCounter = 1;
        int construstorCounter = 1;
//        GeneratedDiagramManager.getInstance().resetRelationCounter();
        for (Element el : tlc.getEnclosedElements()) {
            Object[] modifierElemnts = el.getModifiers().toArray();
            switch (tlc.getKind()) {
                case CLASS:
                    switch (el.getKind()) {
                        case FIELD:
                            Field genField = MemberBuilding.fieldBuilder(el, modifierElemnts);
                            CompilationProcessor.genClass.addField(genField);
                            break;
                        case METHOD:
                            Method genMethod = (Method) MemberBuilding.methodAndConstructorBuilder(el, modifierElemnts, true);
                            try {
                                CompilationProcessor.genClass.addMethod(genMethod);
                            } catch (RuntimeException rtex) {
                                genMethod.setName(genMethod.getName() + "/*" + methodCounter + "*/");
                                CompilationProcessor.genClass.addMethod(genMethod);
                                methodCounter++;
                            }
                            break;
                        case CONSTRUCTOR:
                            Object newConstructor = MemberBuilding.methodAndConstructorBuilder(el, modifierElemnts, false);
                            if (newConstructor instanceof String) {
                                continue;
                            }
                            Constructor genConstr = (Constructor) newConstructor;
                            try {
                                CompilationProcessor.genClass.addConstructor(genConstr);
                            } catch (RuntimeException rtex) {
                                genConstr.setName(genConstr.getName() + "/*" + construstorCounter + "*/");
                                CompilationProcessor.genClass.addConstructor(genConstr);
                                construstorCounter++;
                            }
                            break;
                    }
                    break;
                case ENUM:
                    switch (el.getKind()) {
                        case ENUM_CONSTANT:
                            Literal genliteral = MemberBuilding.literalBuilder(el, modifierElemnts);
                            CompilationProcessor.genEnum.addLiteral(genliteral);
                            break;
                    }
                    break;
                case INTERFACE:
                    Method genMethod = (Method) MemberBuilding.methodAndConstructorBuilder(el, modifierElemnts, true);
                    try {
                        CompilationProcessor.genInterface.addMethod(genMethod);
                    } catch (RuntimeException rtex) {
                        genMethod.setName(genMethod.getName() + "/*" + methodCounter + "*/");
                        CompilationProcessor.genInterface.addMethod(genMethod);
                        methodCounter++;
                    }
                    break;
            }
        }
    }
}