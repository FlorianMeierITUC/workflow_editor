package de.ketobi.vaadinspringdemo.main.ui.navigation.services;

import de.ketobi.vaadinspringdemo.main.ui.navigation.entities.NavigationFolder;
import de.ketobi.vaadinspringdemo.main.ui.navigation.entities.NavigationTarget;
import de.ketobi.vaadinspringdemo.main.ui.navigation.repositories.NavigationFolderRepository;
import de.ketobi.vaadinspringdemo.main.ui.navigation.repositories.NavigationTargetRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class NavigationService {
    private final NavigationFolderRepository folderRepository;
    private final NavigationTargetRepository targetRepository;

    @Autowired
    public NavigationService(NavigationFolderRepository folderRepository, NavigationTargetRepository targetRepository) {
        this.folderRepository = folderRepository;
        this.targetRepository = targetRepository;
    }

    public List<NavigationFolder> getAllNavigationFolders() {
        List<NavigationFolder> folders = folderRepository.findAll();
        folders.sort(Comparator.comparingInt(NavigationFolder::getIndex));
        return folders;
    }

    public List<NavigationTarget> getTargetsByFolderId(ObjectId folderId) {
        List<NavigationTarget> targets = targetRepository.findByIdFolder(folderId);
        targets.sort(Comparator.comparingInt(NavigationTarget::getIndex));
        return targets;
    }

    public void saveNewFolder(String label, Integer index) {
        List<NavigationFolder> folderList = getAllNavigationFolders();
        NavigationFolder newFolder = new NavigationFolder();
        newFolder.setLabel(label);
        newFolder.setIndex(index);
        for (NavigationFolder navFolder : folderList){
            if(navFolder.getIndex() >= newFolder.getIndex()){
                navFolder.setIndex(navFolder.getIndex()+1);
            }
        }
        folderList.add(newFolder);
        folderRepository.saveAll(folderList);
    }

    public void saveNewTarget(ObjectId folderId, String label, String view, Integer index) {
        List<NavigationTarget> targetList = getTargetsByFolderId(folderId);
        NavigationTarget newTarget = new NavigationTarget();
        newTarget.setIdFolder(folderId);
        newTarget.setLabel(label);
        newTarget.setView(view);
        newTarget.setIndex(index);
        for (NavigationTarget navTarget : targetList){
            if(navTarget.getIndex() >= newTarget.getIndex()){
                navTarget.setIndex(navTarget.getIndex()+1);
            }
        }
        targetList.add(newTarget);
        targetRepository.saveAll(targetList);
    }
}