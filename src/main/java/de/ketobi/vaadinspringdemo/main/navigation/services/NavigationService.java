package de.ketobi.vaadinspringdemo.main.navigation.services;

import de.ketobi.vaadinspringdemo.main.navigation.entities.NavigationFolder;
import de.ketobi.vaadinspringdemo.main.navigation.entities.NavigationTarget;
import de.ketobi.vaadinspringdemo.main.navigation.repositories.NavigationFolderRepository;
import de.ketobi.vaadinspringdemo.main.navigation.repositories.NavigationTargetRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return folderRepository.findAll();
    }

    public List<NavigationTarget> getTargetsByFolderId(ObjectId folderId) {
        return targetRepository.findByIdFolder(folderId);
    }
}