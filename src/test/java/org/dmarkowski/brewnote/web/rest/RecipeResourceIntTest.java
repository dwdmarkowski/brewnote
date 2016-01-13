package org.dmarkowski.brewnote.web.rest;

import org.dmarkowski.brewnote.Application;
import org.dmarkowski.brewnote.domain.Recipe;
import org.dmarkowski.brewnote.repository.RecipeRepository;
import org.dmarkowski.brewnote.web.rest.dto.RecipeDTO;
import org.dmarkowski.brewnote.web.rest.mapper.RecipeMapper;

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
 * Test class for the RecipeResource REST controller.
 *
 * @see RecipeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RecipeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_STYLE = "AAAAA";
    private static final String UPDATED_STYLE = "BBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final Double DEFAULT_ABV = 1D;
    private static final Double UPDATED_ABV = 2D;

    private static final Double DEFAULT_ORIGINAL_GRAVITY = 1D;
    private static final Double UPDATED_ORIGINAL_GRAVITY = 2D;
    private static final String DEFAULT_NOTES = "AAAAA";
    private static final String UPDATED_NOTES = "BBBBB";

    private static final Float DEFAULT_VOLUME = 1F;
    private static final Float UPDATED_VOLUME = 2F;

    @Inject
    private RecipeRepository recipeRepository;

    @Inject
    private RecipeMapper recipeMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRecipeMockMvc;

    private Recipe recipe;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RecipeResource recipeResource = new RecipeResource();
        ReflectionTestUtils.setField(recipeResource, "recipeRepository", recipeRepository);
        ReflectionTestUtils.setField(recipeResource, "recipeMapper", recipeMapper);
        this.restRecipeMockMvc = MockMvcBuilders.standaloneSetup(recipeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        recipe = new Recipe();
        recipe.setName(DEFAULT_NAME);
        recipe.setStyle(DEFAULT_STYLE);
        recipe.setDate(DEFAULT_DATE);
        recipe.setAbv(DEFAULT_ABV);
        recipe.setOriginalGravity(DEFAULT_ORIGINAL_GRAVITY);
        recipe.setNotes(DEFAULT_NOTES);
        recipe.setVolume(DEFAULT_VOLUME);
    }

/*    @Test
    @Transactional
    public void createRecipe() throws Exception {
        int databaseSizeBeforeCreate = recipeRepository.findAll().size();

        // Create the Recipe
        RecipeDTO recipeDTO = recipeMapper.recipeToRecipeDTO(recipe);

        restRecipeMockMvc.perform(post("/api/recipes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
                .andExpect(status().isCreated());

        // Validate the Recipe in the database
        List<Recipe> recipes = recipeRepository.findAll();
        assertThat(recipes).hasSize(databaseSizeBeforeCreate + 1);
        Recipe testRecipe = recipes.get(recipes.size() - 1);
        assertThat(testRecipe.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRecipe.getStyle()).isEqualTo(DEFAULT_STYLE);
        assertThat(testRecipe.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRecipe.getAbv()).isEqualTo(DEFAULT_ABV);
        assertThat(testRecipe.getOriginalGravity()).isEqualTo(DEFAULT_ORIGINAL_GRAVITY);
        assertThat(testRecipe.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testRecipe.getVolume()).isEqualTo(DEFAULT_VOLUME);
    }*/

/*    @Test
    @Transactional
    public void getAllRecipes() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get all the recipes
        restRecipeMockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(recipe.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].style").value(hasItem(DEFAULT_STYLE.toString())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].abv").value(hasItem(DEFAULT_ABV.doubleValue())))
                .andExpect(jsonPath("$.[*].originalGravity").value(hasItem(DEFAULT_ORIGINAL_GRAVITY.doubleValue())))
                .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES.toString())))
                .andExpect(jsonPath("$.[*].volume").value(hasItem(DEFAULT_VOLUME.doubleValue())));
    }*/

    @Test
    @Transactional
    public void getRecipe() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

        // Get the recipe
        restRecipeMockMvc.perform(get("/api/recipes/{id}", recipe.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(recipe.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.style").value(DEFAULT_STYLE.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.abv").value(DEFAULT_ABV.doubleValue()))
            .andExpect(jsonPath("$.originalGravity").value(DEFAULT_ORIGINAL_GRAVITY.doubleValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES.toString()))
            .andExpect(jsonPath("$.volume").value(DEFAULT_VOLUME.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRecipe() throws Exception {
        // Get the recipe
        restRecipeMockMvc.perform(get("/api/recipes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRecipe() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

		int databaseSizeBeforeUpdate = recipeRepository.findAll().size();

        // Update the recipe
        recipe.setName(UPDATED_NAME);
        recipe.setStyle(UPDATED_STYLE);
        recipe.setDate(UPDATED_DATE);
        recipe.setAbv(UPDATED_ABV);
        recipe.setOriginalGravity(UPDATED_ORIGINAL_GRAVITY);
        recipe.setNotes(UPDATED_NOTES);
        recipe.setVolume(UPDATED_VOLUME);
        RecipeDTO recipeDTO = recipeMapper.recipeToRecipeDTO(recipe);

        restRecipeMockMvc.perform(put("/api/recipes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(recipeDTO)))
                .andExpect(status().isOk());

        // Validate the Recipe in the database
        List<Recipe> recipes = recipeRepository.findAll();
        assertThat(recipes).hasSize(databaseSizeBeforeUpdate);
        Recipe testRecipe = recipes.get(recipes.size() - 1);
        assertThat(testRecipe.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRecipe.getStyle()).isEqualTo(UPDATED_STYLE);
        assertThat(testRecipe.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRecipe.getAbv()).isEqualTo(UPDATED_ABV);
        assertThat(testRecipe.getOriginalGravity()).isEqualTo(UPDATED_ORIGINAL_GRAVITY);
        assertThat(testRecipe.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testRecipe.getVolume()).isEqualTo(UPDATED_VOLUME);
    }

    @Test
    @Transactional
    public void deleteRecipe() throws Exception {
        // Initialize the database
        recipeRepository.saveAndFlush(recipe);

		int databaseSizeBeforeDelete = recipeRepository.findAll().size();

        // Get the recipe
        restRecipeMockMvc.perform(delete("/api/recipes/{id}", recipe.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Recipe> recipes = recipeRepository.findAll();
        assertThat(recipes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
