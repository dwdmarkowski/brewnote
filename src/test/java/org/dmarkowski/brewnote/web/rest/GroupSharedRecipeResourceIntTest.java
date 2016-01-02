package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.GroupSharedRecipe;
import org.dmarkowski.brewnote.repository.GroupSharedRecipeRepository;
import org.dmarkowski.brewnote.web.rest.dto.GroupSharedRecipeDTO;
import org.dmarkowski.brewnote.web.rest.mapper.GroupSharedRecipeMapper;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the GroupSharedRecipeResource REST controller.
 *
 * @see GroupSharedRecipeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class GroupSharedRecipeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_SHARING_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_SHARING_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SHARING_DATE_STR = dateTimeFormatter.format(DEFAULT_SHARING_DATE);

    @Inject
    private GroupSharedRecipeRepository groupSharedRecipeRepository;

    @Inject
    private GroupSharedRecipeMapper groupSharedRecipeMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restGroupSharedRecipeMockMvc;

    private GroupSharedRecipe groupSharedRecipe;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GroupSharedRecipeResource groupSharedRecipeResource = new GroupSharedRecipeResource();
        ReflectionTestUtils.setField(groupSharedRecipeResource, "groupSharedRecipeRepository", groupSharedRecipeRepository);
        ReflectionTestUtils.setField(groupSharedRecipeResource, "groupSharedRecipeMapper", groupSharedRecipeMapper);
        this.restGroupSharedRecipeMockMvc = MockMvcBuilders.standaloneSetup(groupSharedRecipeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        groupSharedRecipe = new GroupSharedRecipe();
        groupSharedRecipe.setSharingDate(DEFAULT_SHARING_DATE);
    }

    @Test
    @Transactional
    public void createGroupSharedRecipe() throws Exception {
        int databaseSizeBeforeCreate = groupSharedRecipeRepository.findAll().size();

        // Create the GroupSharedRecipe
        GroupSharedRecipeDTO groupSharedRecipeDTO = groupSharedRecipeMapper.groupSharedRecipeToGroupSharedRecipeDTO(groupSharedRecipe);

        restGroupSharedRecipeMockMvc.perform(post("/api/groupSharedRecipes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groupSharedRecipeDTO)))
                .andExpect(status().isCreated());

        // Validate the GroupSharedRecipe in the database
        List<GroupSharedRecipe> groupSharedRecipes = groupSharedRecipeRepository.findAll();
        assertThat(groupSharedRecipes).hasSize(databaseSizeBeforeCreate + 1);
        GroupSharedRecipe testGroupSharedRecipe = groupSharedRecipes.get(groupSharedRecipes.size() - 1);
        assertThat(testGroupSharedRecipe.getSharingDate()).isEqualTo(DEFAULT_SHARING_DATE);
    }

    @Test
    @Transactional
    public void getAllGroupSharedRecipes() throws Exception {
        // Initialize the database
        groupSharedRecipeRepository.saveAndFlush(groupSharedRecipe);

        // Get all the groupSharedRecipes
        restGroupSharedRecipeMockMvc.perform(get("/api/groupSharedRecipes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(groupSharedRecipe.getId().intValue())))
                .andExpect(jsonPath("$.[*].sharingDate").value(hasItem(DEFAULT_SHARING_DATE_STR)));
    }

    @Test
    @Transactional
    public void getGroupSharedRecipe() throws Exception {
        // Initialize the database
        groupSharedRecipeRepository.saveAndFlush(groupSharedRecipe);

        // Get the groupSharedRecipe
        restGroupSharedRecipeMockMvc.perform(get("/api/groupSharedRecipes/{id}", groupSharedRecipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(groupSharedRecipe.getId().intValue()))
            .andExpect(jsonPath("$.sharingDate").value(DEFAULT_SHARING_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingGroupSharedRecipe() throws Exception {
        // Get the groupSharedRecipe
        restGroupSharedRecipeMockMvc.perform(get("/api/groupSharedRecipes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupSharedRecipe() throws Exception {
        // Initialize the database
        groupSharedRecipeRepository.saveAndFlush(groupSharedRecipe);

		int databaseSizeBeforeUpdate = groupSharedRecipeRepository.findAll().size();

        // Update the groupSharedRecipe
        groupSharedRecipe.setSharingDate(UPDATED_SHARING_DATE);
        GroupSharedRecipeDTO groupSharedRecipeDTO = groupSharedRecipeMapper.groupSharedRecipeToGroupSharedRecipeDTO(groupSharedRecipe);

        restGroupSharedRecipeMockMvc.perform(put("/api/groupSharedRecipes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(groupSharedRecipeDTO)))
                .andExpect(status().isOk());

        // Validate the GroupSharedRecipe in the database
        List<GroupSharedRecipe> groupSharedRecipes = groupSharedRecipeRepository.findAll();
        assertThat(groupSharedRecipes).hasSize(databaseSizeBeforeUpdate);
        GroupSharedRecipe testGroupSharedRecipe = groupSharedRecipes.get(groupSharedRecipes.size() - 1);
        assertThat(testGroupSharedRecipe.getSharingDate()).isEqualTo(UPDATED_SHARING_DATE);
    }

    @Test
    @Transactional
    public void deleteGroupSharedRecipe() throws Exception {
        // Initialize the database
        groupSharedRecipeRepository.saveAndFlush(groupSharedRecipe);

		int databaseSizeBeforeDelete = groupSharedRecipeRepository.findAll().size();

        // Get the groupSharedRecipe
        restGroupSharedRecipeMockMvc.perform(delete("/api/groupSharedRecipes/{id}", groupSharedRecipe.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<GroupSharedRecipe> groupSharedRecipes = groupSharedRecipeRepository.findAll();
        assertThat(groupSharedRecipes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
