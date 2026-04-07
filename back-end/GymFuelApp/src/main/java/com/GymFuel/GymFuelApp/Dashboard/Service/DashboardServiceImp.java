package com.GymFuel.GymFuelApp.Dashboard.Service;

import com.GymFuel.GymFuelApp.Dashboard.DashboardDao;
import com.GymFuel.GymFuelApp.Dashboard.DashboardDto;
import com.GymFuel.GymFuelApp.Dashboard.DashboardEntity;
import com.GymFuel.GymFuelApp.Member.DAO.MemberRepository;
import com.GymFuel.GymFuelApp.Member.Entity.RegisterMemberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DashboardServiceImp implements DashboardService {
    private MemberRepository memberRepository;
    private DashboardDao dashboardDao;

    @Autowired
    DashboardServiceImp(MemberRepository memberRepository, DashboardDao dashboardDao) {
        this.memberRepository = memberRepository;
        this.dashboardDao = dashboardDao;
    }

    @Override
    public DashboardDto getDashboardData(String email) {


        // 1. Look for the member
        RegisterMemberEntity member = memberRepository.findMemberByEmail(email);

        // 2. Look for their dashboard state
        Optional<DashboardEntity> stateCheck = dashboardDao.findByEmail(email);

        DashboardEntity state;

        if (stateCheck.isPresent()) {
            // "I found it! Use the existing one."
            state = stateCheck.get();
        } else {
            // "It's missing! Let's make a new one."
            state = new DashboardEntity();
            state.setUser(member);
            dashboardDao.save(state);
        }

        return new DashboardDto(member.getName(), state.isGymSetupComplete(), state.isDietSetupComplete());
    }
}
