package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HealthConditions
import com.example.ui.components.PrimaryFitButton
import com.example.ui.components.TopBarWithBack
import com.example.ui.theme.FitLimeDark

@Composable
fun AnamneseScreen(
    conditions: HealthConditions,
    onBackClick: () -> Unit,
    onNextClick: (HealthConditions) -> Unit
) {
    var hypertension by remember { mutableStateOf(conditions.hypertension) }
    var diabetes by remember { mutableStateOf(conditions.diabetes) }
    var heartConditions by remember { mutableStateOf(conditions.heartConditions) }
    var arthrosisArthritis by remember { mutableStateOf(conditions.arthrosisArthritis) }
    var osteoporosis by remember { mutableStateOf(conditions.osteoporosis) }
    var obesity by remember { mutableStateOf(conditions.obesity) }
    var others by remember { mutableStateOf(conditions.others) }
    var notes by remember { mutableStateOf(conditions.notes) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .systemBarsPadding()
            .testTag("anamnese_screen")
    ) {
        TopBarWithBack(
            title = "Anamnese",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Condições de saúde",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "(Selecione as opções que se aplicam):",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    AnamneseCheckboxRow(
                        label = "Hipertensão",
                        checked = hypertension,
                        onCheckedChange = { hypertension = it }
                    )
                    AnamneseCheckboxRow(
                        label = "Diabetes",
                        checked = diabetes,
                        onCheckedChange = { diabetes = it }
                    )
                    AnamneseCheckboxRow(
                        label = "Problemas cardíacos",
                        checked = heartConditions,
                        onCheckedChange = { heartConditions = it }
                    )
                    AnamneseCheckboxRow(
                        label = "Artrose / Artrite",
                        checked = arthrosisArthritis,
                        onCheckedChange = { arthrosisArthritis = it }
                    )
                    AnamneseCheckboxRow(
                        label = "Osteoporose",
                        checked = osteoporosis,
                        onCheckedChange = { osteoporosis = it }
                    )
                    AnamneseCheckboxRow(
                        label = "Obesidade",
                        checked = obesity,
                        onCheckedChange = { obesity = it }
                    )
                    AnamneseCheckboxRow(
                        label = "Outras",
                        checked = others,
                        onCheckedChange = { others = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Observações",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                placeholder = { Text("Digite aqui...") },
                minLines = 3,
                maxLines = 5,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = FitLimeDark,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("anamnese_notes_input")
            )

            Spacer(modifier = Modifier.height(28.dp))

            PrimaryFitButton(
                text = "Próximo",
                onClick = {
                    onNextClick(
                        HealthConditions(
                            hypertension = hypertension,
                            diabetes = diabetes,
                            heartConditions = heartConditions,
                            arthrosisArthritis = arthrosisArthritis,
                            osteoporosis = osteoporosis,
                            obesity = obesity,
                            others = others,
                            notes = notes
                        )
                    )
                },
                testTag = "anamnese_next_button"
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun AnamneseCheckboxRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = FitLimeDark)
        )
        Text(
            text = label,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}
