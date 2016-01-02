package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.FriendGroup;
import org.dmarkowski.brewnote.repository.FriendGroupRepository;
import org.dmarkowski.brewnote.web.rest.dto.FriendGroupDTO;
import org.dmarkowski.brewnote.web.rest.mapper.FriendGroupMapper;

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
 * Test class for the FriendGroupResource REST controller.
 *
 * @see FriendGroupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FriendGroupResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private FriendGroupRepository friendGroupRepository;

    @Inject
    private FriendGroupMapper friendGroupMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFriendGroupMockMvc;

    private FriendGroup friendGroup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FriendGroupResource friendGroupResource = new FriendGroupResource();
        ReflectionTestUtils.setField(friendGroupResource, "friendGroupRepository", friendGroupRepository);
        ReflectionTestUtils.setField(friendGroupResource, "friendGroupMapper", friendGroupMapper);
        this.restFriendGroupMockMvc = MockMvcBuilders.standaloneSetup(friendGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        friendGroup = new FriendGroup();
        friendGroup.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFriendGroup() throws Exception {
        int databaseSizeBeforeCreate = friendGroupRepository.findAll().size();

        // Create the FriendGroup
        FriendGroupDTO friendGroupDTO = friendGroupMapper.friendGroupToFriendGroupDTO(friendGroup);

        restFriendGroupMockMvc.perform(post("/api/friendGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(friendGroupDTO)))
                .andExpect(status().isCreated());

        // Validate the FriendGroup in the database
        List<FriendGroup> friendGroups = friendGroupRepository.findAll();
        assertThat(friendGroups).hasSize(databaseSizeBeforeCreate + 1);
        FriendGroup testFriendGroup = friendGroups.get(friendGroups.size() - 1);
        assertThat(testFriendGroup.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllFriendGroups() throws Exception {
        // Initialize the database
        friendGroupRepository.saveAndFlush(friendGroup);

        // Get all the friendGroups
        restFriendGroupMockMvc.perform(get("/api/friendGroups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(friendGroup.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFriendGroup() throws Exception {
        // Initialize the database
        friendGroupRepository.saveAndFlush(friendGroup);

        // Get the friendGroup
        restFriendGroupMockMvc.perform(get("/api/friendGroups/{id}", friendGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(friendGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFriendGroup() throws Exception {
        // Get the friendGroup
        restFriendGroupMockMvc.perform(get("/api/friendGroups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFriendGroup() throws Exception {
        // Initialize the database
        friendGroupRepository.saveAndFlush(friendGroup);

		int databaseSizeBeforeUpdate = friendGroupRepository.findAll().size();

        // Update the friendGroup
        friendGroup.setName(UPDATED_NAME);
        FriendGroupDTO friendGroupDTO = friendGroupMapper.friendGroupToFriendGroupDTO(friendGroup);

        restFriendGroupMockMvc.perform(put("/api/friendGroups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(friendGroupDTO)))
                .andExpect(status().isOk());

        // Validate the FriendGroup in the database
        List<FriendGroup> friendGroups = friendGroupRepository.findAll();
        assertThat(friendGroups).hasSize(databaseSizeBeforeUpdate);
        FriendGroup testFriendGroup = friendGroups.get(friendGroups.size() - 1);
        assertThat(testFriendGroup.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFriendGroup() throws Exception {
        // Initialize the database
        friendGroupRepository.saveAndFlush(friendGroup);

		int databaseSizeBeforeDelete = friendGroupRepository.findAll().size();

        // Get the friendGroup
        restFriendGroupMockMvc.perform(delete("/api/friendGroups/{id}", friendGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<FriendGroup> friendGroups = friendGroupRepository.findAll();
        assertThat(friendGroups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
