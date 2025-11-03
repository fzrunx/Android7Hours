package com.sesac.common.component

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.sesac.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColumnScope.CommonSearchBar(
    alignment: Alignment.Horizontal,
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    searchResultsScrollState: ScrollState? = null,
    space: Dp = dimensionResource(R.dimen.default_space),
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val columnModifier = searchResultsScrollState
        ?.let { Modifier.verticalScroll(searchResultsScrollState) }
        ?: Modifier

    SearchBar(
        modifier = Modifier
            .align(alignment)
            .fillMaxWidth()
            .padding(horizontal = space)
            .semantics { traversalIndex = -1f },
        colors = SearchBarDefaults.colors(inputFieldColors = TextFieldDefaults.colors(Color.Black)),
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = { textFieldState.edit { replace(0, Int.MAX_VALUE, it) } },
                onSearch = {
                    onSearch(textFieldState.text.toString())
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search") }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        // Display search results in a scrollable column
        Column(
            columnModifier
        ) {
            searchResults.forEach { result ->
                ListItem(
                    headlineContent = { Text(result) },
                    modifier = Modifier
                        .clickable {
                            textFieldState.edit { replace(0, length, result) }
                            expanded = false
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.CommonSearchBar(
    alignment: Alignment,
    textFieldState: TextFieldState,
    onSearch: (String) -> Unit,
    searchResults: List<String>,
    searchResultsScrollState: ScrollState = rememberScrollState(),
    space: Dp = dimensionResource(R.dimen.default_space),
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    SearchBar(
        modifier = Modifier
            .align(alignment)
            .fillMaxWidth()
            .padding(space)
            .semantics { traversalIndex = -1f },
        colors = SearchBarDefaults.colors(inputFieldColors = TextFieldDefaults.colors(Color.Black)),
        inputField = {
            SearchBarDefaults.InputField(
                query = textFieldState.text.toString(),
                onQueryChange = { textFieldState.edit { replace(0, Int.MAX_VALUE, it) } },
                onSearch = {
                    onSearch(textFieldState.text.toString())
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search") }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        // Display search results in a scrollable column
        Column(Modifier.verticalScroll(searchResultsScrollState)) {
            searchResults.forEach { result ->
                ListItem(
                    headlineContent = { Text(result) },
                    modifier = Modifier
                        .clickable {
                            textFieldState.edit { replace(0, length, result) }
                            expanded = false
                        }
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommonSearchBarPreview(){
    val onSearch = {s: String -> Unit }
    MaterialTheme {
        Column {
            CommonSearchBar(
                Alignment.CenterHorizontally,
                TextFieldState(),
                onSearch,
                listOf("a", "b", "c")
                )
        }
    }
}