package se.group.backendgruppuppgift.tasker.service;

import org.springframework.stereotype.Service;
import se.group.backendgruppuppgift.tasker.model.Team;
import se.group.backendgruppuppgift.tasker.model.User;
import se.group.backendgruppuppgift.tasker.model.web.TeamWeb;
import se.group.backendgruppuppgift.tasker.repository.TeamRepository;
import se.group.backendgruppuppgift.tasker.repository.UserRepository;
import se.group.backendgruppuppgift.tasker.service.exception.InvalidTeamException;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;

@Service
public final class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public TeamWeb createTeam(TeamWeb team) {
        Team result = teamRepository.save(new Team(team.getName(), team.getIsActive()));

        return convertToWeb(result);
    }

    public Optional<TeamWeb> findTeam(Long id) {
        Optional<Team> result = teamRepository.findById(id);

        if (result.isPresent())
            return Optional.ofNullable(convertToWeb(result.get()));

        return Optional.empty();
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }


    public Optional<TeamWeb> updateTeam(Long id, TeamWeb team) {
        Optional<Team> result = teamRepository.findById(id);

        if (result.isPresent()) {
            Team updatedTeam = result.get();

            if (!isBlank(team.getName()))
                updatedTeam.setName(team.getName());

            if (team.getIsActive() != null)
                updatedTeam.setIsActive(team.getIsActive());

            return Optional.ofNullable(convertToWeb(teamRepository.save(updatedTeam)));
        }

        return Optional.empty();
    }

    public Optional<TeamWeb> deleteTeam(Long id) {
        Optional<Team> result = teamRepository.findById(id);

        if (result.isPresent()) {
            teamRepository.deleteById(id);

            return Optional.ofNullable(convertToWeb(result.get()));
        }

        return Optional.empty();
    }

    private TeamWeb convertToWeb(Team team) {
        return new TeamWeb(team.getId(), team.getName(), team.getIsActive());
    }
}
