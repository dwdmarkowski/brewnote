package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.UserGroup;
import org.dmarkowski.brewnote.repository.UserGroupRepository;
import org.dmarkowski.brewnote.web.rest.dto.UserGroupDTO;
import org.dmarkowski.brewnote.web.rest.mapper.UserGroupMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the UserGroupResource REST controller.
 *
 * @see UserGroupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserGroupResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";
    private static final String DEFAULT_ROLE = "AAAAA";
    private static final String UPDATED_ROLE = "BBBBB";

    @Inject
    private UserGroupRepository userGroupRepository;

    @Inject
    private UserGroupMapper userGroupMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserGroupMockMvc;

    private UserGroup userGroup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserGroupResource userGroupResource = new UserGroupResource();
        ReflectionTestUtils.setField(userGroupResource, "userGroupRepository", userGroupRepository);
        ReflectionTestUtils.setField(userGroupResource, "userGroupMapper", userGroupMapper);
        this.restUserGroupMockMvc = MockMvcBuilders.standaloneSetup(userGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userGroup = new UserGroup();
        userGroup.setStatus(DEFAULT_STATUS);
        userGroup.setRole(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void createUserGroup() throws Exception {
        int databaseSizeBeforeCreate = userGroupRepository.findAll().size();

        // Create the UserGroup
        UserGroupDTO userGroupDTO = userGroupMapper.userGroupToUserGroupDTO(userGroup);

        restUserGroupMockMvc.perform(post("/api/userGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userGroupDTO)))
                .andExpect(status().isCreated());

        // Validate the UserGroup in the database
        List<UserGroup> userGroups = userGroupRepository.findAll();
        assertThat(userGroups).hasSize(databaseSizeBeforeCreate + 1);
        UserGroup testUserGroup = userGroups.get(userGroups.size() - 1);
        assertThat(testUserGroup.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testUserGroup.getRole()).isEqualTo(DEFAULT_ROLE);
    }

    @Test
    @Transactional
    public void getAllUserGroups() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get all the userGroups
        restUserGroupMockMvc.perform(get("/api/userGroups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userGroup.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
                .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())));
    }

    @Test
    @Transactional
    public void getUserGroup() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

        // Get the userGroup
        restUserGroupMockMvc.perform(get("/api/userGroups/{id}", userGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userGroup.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserGroup() throws Exception {
        // Get the userGroup
        restUserGroupMockMvc.perform(get("/api/userGroups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserGroup() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

		int databaseSizeBeforeUpdate = userGroupRepository.findAll().size();

        // Update the userGroup
        userGroup.setStatus(UPDATED_STATUS);
        userGroup.setRole(UPDATED_ROLE);
        UserGroupDTO userGroupDTO = userGroupMapper.userGroupToUserGroupDTO(userGroup);

        restUserGroupMockMvc.perform(put("/api/userGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userGroupDTO)))
                .andExpect(status().isOk());

        // Validate the UserGroup in the database
        List<UserGroup> userGroups = userGroupRepository.findAll();
        assertThat(userGroups).hasSize(databaseSizeBeforeUpdate);
        UserGroup testUserGroup = userGroups.get(userGroups.size() - 1);
        assertThat(testUserGroup.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testUserGroup.getRole()).isEqualTo(UPDATED_ROLE);
    }

    @Test
    @Transactional
    public void deleteUserGroup() throws Exception {
        // Initialize the database
        userGroupRepository.saveAndFlush(userGroup);

		int databaseSizeBeforeDelete = userGroupRepository.findAll().size();

        // Get the userGroup
        restUserGroupMockMvc.perform(delete("/api/userGroups/{id}", userGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserGroup> userGroups = userGroupRepository.findAll();
        assertThat(userGroups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
