package com.example.portalapplication.controllers.AdminControllers;

import com.example.portalapplication.models.Submission;
import com.example.portalapplication.models.Team;
import com.example.portalapplication.services.SubmissionService;
import com.example.portalapplication.services.TeamService;
import com.example.portalapplication.services.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/submissions")
public class AdminSubmissionController {
    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private VersionService versionService;

    // LIST + SEARCH
    @GetMapping
    public String listSubmissions(
            @RequestParam(value = "search", required = false) String search,
            Model model
    ) {
        List<Submission> submissions = submissionService.search(search);
        model.addAttribute("submissions", submissions);
        model.addAttribute("search", search == null ? "" : search);
        return "admin/submissions/list";
    }
    @GetMapping("/{id}")
    public String viewSubmission(
            @PathVariable Integer id,
            Model model
    ) {
        model.addAttribute("submission", submissionService.findById(id));
        model.addAttribute("versions", versionService.findAllBySubmissionId(id));

        return "admin/submissions/view";
    }
    // CREATE FORM
    @GetMapping("/create")
    public String showCreateForm(@RequestParam Integer teamId,
                                 Model model) {
        model.addAttribute("submission", new Submission());

        // students dropdown
        model.addAttribute("members", teamService.findById(teamId).getMembers());
        model.addAttribute("teamId", teamId);
        model.addAttribute("isEdit", false);
        model.addAttribute("formAction", "/admin/submissions/create");

        return "admin/submissions/form";
    }

    // CREATE POST
    @PostMapping("/create")
    public String saveSubmission(@ModelAttribute("submission") Submission submission,
                                @RequestParam Integer studentId,
                                 @RequestParam Integer teamId) {
        submissionService.saveSubmission(submission,studentId,teamId);

        return "redirect:/admin/teams/" + teamId;
    }


    // EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Submission submission = submissionService.findById(id);
        if (submission == null) {
            return "redirect:/admin/submissions";
        }

        model.addAttribute("submission", submission);
        // dropdown members
        model.addAttribute("members", submission.getTeam().getMembers());
        model.addAttribute("teamId", submission.getTeam().getId());
        model.addAttribute("isEdit", true);
        model.addAttribute("formAction", "/admin/submissions/edit");
        return "admin/submissions/form";
    }


    // UPDATE POST
    @PostMapping("/edit")
    public String updateSubmission(@ModelAttribute("submission") Submission updatedSubmission,
                                   @RequestParam Integer studentId,
                                   @RequestParam Integer teamId) {
        submissionService.saveSubmission(updatedSubmission,studentId,teamId);
        return "redirect:/admin/submissions/" + updatedSubmission.getId();
    }

    // DELETE
    @PostMapping("/delete/{id}")
    public String deleteSubmission(@PathVariable int id) {
        submissionService.deleteSubmission(id);
        return "redirect:/admin/submissions";
    }
}
