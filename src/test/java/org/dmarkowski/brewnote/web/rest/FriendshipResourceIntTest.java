package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.Friendship;
import org.dmarkowski.brewnote.repository.FriendshipRepository;
import org.dmarkowski.brewnote.web.rest.dto.FriendshipDTO;
import org.dmarkowski.brewnote.web.rest.mapper.FriendshipMapper;

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
 * Test class for the FriendshipResource REST controller.
 *
 * @see FriendshipResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FriendshipResourceIntTest {

    private static final String DEFAULT_STATUS = "AAAAA";
    private static final String UPDATED_STATUS = "BBBBB";

    @Inject
    private FriendshipRepository friendshipRepository;

    @Inject
    private FriendshipMapper friendshipMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFriendshipMockMvc;

    private Friendship friendship;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FriendshipResource friendshipResource = new FriendshipResource();
        ReflectionTestUtils.setField(friendshipResource, "friendshipRepository", friendshipRepository);
        ReflectionTestUtils.setField(friendshipResource, "friendshipMapper", friendshipMapper);
        this.restFriendshipMockMvc = MockMvcBuilders.standaloneSetup(friendshipResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        friendship = new Friendship();
        friendship.setStatus(DEFAULT_STATUS);
    }

/*    @Test
    @Transactional
    public void createFriendship() throws Exception {
        int databaseSizeBeforeCreate = friendshipRepository.findAll().size();

        // Create the Friendship
        FriendshipDTO friendshipDTO = friendshipMapper.friendshipToFriendshipDTO(friendship);

        restFriendshipMockMvc.perform(post("/api/friendships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(friendshipDTO)))
                .andExpect(status().isCreated());

        // Validate the Friendship in the database
        List<Friendship> friendships = friendshipRepository.findAll();
        assertThat(friendships).hasSize(databaseSizeBeforeCreate + 1);
        Friendship testFriendship = friendships.get(friendships.size() - 1);
        assertThat(testFriendship.getStatus()).isEqualTo(DEFAULT_STATUS);
    }*/

/*    @Test
    @Transactional
    public void getAllFriendships() throws Exception {
        // Initialize the database
        friendshipRepository.saveAndFlush(friendship);

        // Get all the friendships
        restFriendshipMockMvc.perform(get("/api/friendships"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(friendship.getId().intValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }*/

    @Test
    @Transactional
    public void getFriendship() throws Exception {
        // Initialize the database
        friendshipRepository.saveAndFlush(friendship);

        // Get the friendship
        restFriendshipMockMvc.perform(get("/api/friendships/{id}", friendship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(friendship.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFriendship() throws Exception {
        // Get the friendship
        restFriendshipMockMvc.perform(get("/api/friendships/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

/*    @Test
    @Transactional
    public void updateFriendship() throws Exception {
        // Initialize the database
        friendshipRepository.saveAndFlush(friendship);

		int databaseSizeBeforeUpdate = friendshipRepository.findAll().size();

        // Update the friendship
        friendship.setStatus(UPDATED_STATUS);
        FriendshipDTO friendshipDTO = friendshipMapper.friendshipToFriendshipDTO(friendship);

        restFriendshipMockMvc.perform(put("/api/friendships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(friendshipDTO)))
                .andExpect(status().isOk());

        // Validate the Friendship in the database
        List<Friendship> friendships = friendshipRepository.findAll();
        assertThat(friendships).hasSize(databaseSizeBeforeUpdate);
        Friendship testFriendship = friendships.get(friendships.size() - 1);
        assertThat(testFriendship.getStatus()).isEqualTo(UPDATED_STATUS);
    }*/

    @Test
    @Transactional
    public void deleteFriendship() throws Exception {
        // Initialize the database
        friendshipRepository.saveAndFlush(friendship);

		int databaseSizeBeforeDelete = friendshipRepository.findAll().size();

        // Get the friendship
        restFriendshipMockMvc.perform(delete("/api/friendships/{id}", friendship.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Friendship> friendships = friendshipRepository.findAll();
        assertThat(friendships).hasSize(databaseSizeBeforeDelete - 1);
    }
}
