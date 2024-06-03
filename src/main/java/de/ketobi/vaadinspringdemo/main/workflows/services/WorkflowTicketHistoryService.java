package de.ketobi.vaadinspringdemo.main.workflows.services;

import de.ketobi.vaadinspringdemo.main.workflows.entities.WorkflowTicketHistory;
import de.ketobi.vaadinspringdemo.main.workflows.repositories.WorkflowTicketHistoryRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WorkflowTicketHistoryService {
    private final WorkflowTicketHistoryRepository workflowTicketHistoryRepository;

    public WorkflowTicketHistoryService(WorkflowTicketHistoryRepository workflowTicketHistoryRepository) {
        this.workflowTicketHistoryRepository = workflowTicketHistoryRepository;
    }

    public void save(WorkflowTicketHistory workflowTicketHistory) {
        workflowTicketHistoryRepository.save(workflowTicketHistory);
    }

    public ArrayList<WorkflowTicketHistory> getHistoryEntries(ObjectId ticketId) {
        return workflowTicketHistoryRepository.findByTicketId(ticketId);
    }

}
