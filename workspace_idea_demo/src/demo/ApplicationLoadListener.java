package demo;

import com.intellij.ide.ApplicationLoadListener;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.roots.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.util.indexing.FileBasedIndex;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Created by Administrator on 2017/4/3 0003.
 */
class MyApplicationLoadListener implements ApplicationLoadListener {

    public MyApplicationLoadListener()
    {

    }

    @Override
    public void beforeApplicationLoaded(@NotNull Application application, @NotNull String configPath) {
    }

    @Override
    public void beforeComponentsCreated() {

    }
}
