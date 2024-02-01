import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.marvellibrary.CharacterImage
import com.example.marvellibrary.Destination
import com.example.marvellibrary.comicsToString
import com.example.marvellibrary.viewmodel.CollectionDbViewModel
import com.example.marvellibrary.viewmodel.MarvelApiViewModel

@Composable
fun CharacterDetailsScreen(
    marvelApiViewModel: MarvelApiViewModel,
    collectionDbViewModel: CollectionDbViewModel,
    paddingValues: PaddingValues,
    navController: NavHostController
) {
    val character = marvelApiViewModel.characterDetails.value
    val collection by collectionDbViewModel.collection.collectAsState()
    val inCollection = collection.map { it.apiId }.contains(character?.id)

    if (character == null) {
        navController.navigate(Destination.Library.route) {
            popUpTo(Destination.Library.route)
            launchSingleTop = true
        }
    }

    LaunchedEffect(key1 = Unit) {
        collectionDbViewModel.setCurrentCharacterId(character?.id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp)
            .padding(bottom = paddingValues.calculateBottomPadding())
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageUrl = character?.thumbnail?.path + "." + character?.thumbnail?.extension
        val title = character?.name ?: "No Name"
        val comics =
            character?.comics?.items?.mapNotNull { it.name }?.comicsToString() ?: "No comics"
        val description = character?.description ?: "No description"

        CharacterImage(
            url = imageUrl, modifier = Modifier
                .width(200.dp)
                .padding(4.dp)
        )

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier.padding(4.dp),
        )

        Text(
            text = comics,
            fontStyle = FontStyle.Italic,
            fontSize = 12.sp,
            modifier = Modifier.padding(4.dp)
        )

        Text(
            text = description,
            fontSize = 16.sp,
            modifier = Modifier.padding(4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (!inCollection && character != null) {
                collectionDbViewModel.addCharacter(character)
            }
        }, modifier = Modifier.padding(bottom = 20.dp)) {
            if(!inCollection) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text(text = "Add to collection")
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Text(text = "Added")
                }
            }
        }
    }

}