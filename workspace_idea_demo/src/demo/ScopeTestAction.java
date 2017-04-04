package demo;

import com.google.common.collect.Lists;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.module.impl.scopes.LibraryRuntimeClasspathScope;
import com.intellij.openapi.module.impl.scopes.LibraryScope;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.Processor;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Created by Administrator on 2017/4/3 0003.
 */
public class ScopeTestAction extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Project project = ProjectManager.getInstance().getOpenProjects()[0];
        ModuleManager moduleManager = ModuleManager.getInstance(project);
        Module parent1 = moduleManager.findModuleByName("parent1");
        Module parent2 = moduleManager.findModuleByName("parent2");
        Module module1_1 = moduleManager.findModuleByName("module1_1");
        Module module2_1 = moduleManager.findModuleByName("module2_1");
        List<Module> modules = Lists.newArrayList(module1_1, module2_1);

        //module+dependencies module
        searchFile(project, module2_1, GlobalSearchScope.moduleWithDependenciesScope(module2_1), "moduleWithDependenciesScope");
        //module+libs（include exported libs in dependencies module）
        searchFile(project, module2_1, GlobalSearchScope.moduleWithLibrariesScope(module2_1), "moduleWithLibrariesScope");
        //module+dependencies module+libs
        searchFile(project, module2_1, GlobalSearchScope.moduleWithDependenciesAndLibrariesScope(module2_1), "moduleWithDependenciesAndLibrariesScope");
        //module+dependencies module+their libs
        searchFile(project, module2_1, GlobalSearchScope.moduleRuntimeScope(module2_1, false), "moduleRuntimeScope");

        searchFile(project, module2_1, new LibraryRuntimeClasspathScope(project, Lists.newArrayList(module2_1)), "LibraryRuntimeClasspathScope");

        searchFile(project, module2_1, getSelfScope(modules), "getSelfScope");
        searchFile(project, module2_1, getLibraryScope(project, modules), "getLibraryScope");
        searchFile(project, module2_1, getAllScope(modules), "getAllScope");
    }

    private void searchFile(Project project, Module module, GlobalSearchScope scope, String msg) {
        System.out.println(msg);
        Collection<VirtualFile> virtualFilesByName = FilenameIndex.getVirtualFilesByName(project, "test.MF", scope);
        for (VirtualFile file : virtualFilesByName) {
            System.out.println(file.getPath());
        }
    }

    private GlobalSearchScope getSelfScope(List<Module> modules) {
        List<Library> libraryList = Lists.newArrayList();
        GlobalSearchScope[] moduleScopes = new GlobalSearchScope[modules.size()];
        int i = 0;
        for (Module module : modules) {
            moduleScopes[i++] = GlobalSearchScope.moduleScope(module);
        }
        return GlobalSearchScope.union(moduleScopes);
    }

    @NotNull
    private GlobalSearchScope getLibraryScope(Project project, List<Module> modules) {
        List<Library> libraryList = Lists.newArrayList();
        for (Module module : modules) {
            ModuleRootManager.getInstance(module).orderEntries().forEachLibrary(new Processor<Library>() {
                @Override
                public boolean process(Library entry) {
                    libraryList.add(entry);
                    return true;
                }
            });
        }

        GlobalSearchScope[] libraryScopes = new GlobalSearchScope[libraryList.size()];
        int i = 0;
        for (Library library : libraryList) {
            libraryScopes[i++] = new LibraryScope(project, library);
        }
        return GlobalSearchScope.union(libraryScopes);
    }

    @NotNull
    private GlobalSearchScope getAllScope(List<Module> modules) {
        GlobalSearchScope[] moduleScopes = new GlobalSearchScope[modules.size()];
        int i = 0;
        for (Module module : modules) {
            moduleScopes[i++] = GlobalSearchScope.moduleWithLibrariesScope(module);
        }
        return GlobalSearchScope.union(moduleScopes);
    }

}
