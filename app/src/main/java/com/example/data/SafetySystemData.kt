package com.example.data

object SafetySystemData {

    val symptomsList: List<PreWorkoutSymptom> = listOf(
        PreWorkoutSymptom(
            id = "tontura",
            title = "Tontura",
            subtitle = "Sensação de cabeça oca, vertigem, teto rodando ou instabilidade ao levantar.",
            emoji = "💫",
            isEmergency = false,
            clinicalRiskNote = "A tontura pré-esforço aumenta drasticamente o risco de quedas e desmaios por hipotensão ortostática."
        ),
        PreWorkoutSymptom(
            id = "dor_peito",
            title = "Dor no peito",
            subtitle = "Sensação de aperto, peso, queimação no peito que pode irradiar para braço, mandíbula ou costas.",
            emoji = "🫀",
            isEmergency = true,
            clinicalRiskNote = "Sinal vermelho clássico. Pode indicar evento isquêmico cardíaco agudo. Requer atendimento médico imediato."
        ),
        PreWorkoutSymptom(
            id = "falta_ar",
            title = "Falta de ar fora do habitual",
            subtitle = "Dificuldade para respirar mesmo em repouso ou ao dar passos leves no ambiente.",
            emoji = "🫁",
            isEmergency = true,
            clinicalRiskNote = "Pode sinalizar descompensação cardiorrespiratória aguda, crise asmática ou arritmia cardíaca."
        ),
        PreWorkoutSymptom(
            id = "mal_estar",
            title = "Mal-estar",
            subtitle = "Sensação súbita de fraqueza, náusea, sudorese fria, palidez ou cansaço inexplicável.",
            emoji = "🤢",
            isEmergency = false,
            clinicalRiskNote = "Comum em hipoglicemia aguda em diabéticos ou variações bruscas de pressão arterial."
        ),
        PreWorkoutSymptom(
            id = "queda_recente",
            title = "Queda recente",
            subtitle = "Queda ou tropeço com impacto no solo ou móveis ocorrido nas últimas 48 horas.",
            emoji = "⚠️",
            isEmergency = false,
            clinicalRiskNote = "Necessário excluir fraturas subclínicas, traumatismo craniano leve ou hematomas musculares antes de qualquer carga."
        ),
        PreWorkoutSymptom(
            id = "dor_intensa",
            title = "Dor intensa",
            subtitle = "Dor articular, muscular ou na coluna que começou hoje ou piorou de forma expressiva.",
            emoji = "⚡",
            isEmergency = false,
            clinicalRiskNote = "O exercício sobre estruturas em crise inflamatória aguda pode agravar a lesão tecidual."
        ),
        PreWorkoutSymptom(
            id = "alteracao_importante",
            title = "Alteração importante percebida",
            subtitle = "Visão embaçada súbita, perda de força em um braço/perna, boca torta, confusão mental ou palpitação acelerada.",
            emoji = "👁️",
            isEmergency = true,
            clinicalRiskNote = "Sinais compatíveis com alterações vasculares cerebrais ou arritmias. Exige avaliação médica presencial urgente."
        )
    )

    val defaultRecords: List<SafetyCheckRecord> = listOf(
        SafetyCheckRecord(
            id = "rec_001",
            timestamp = System.currentTimeMillis() - 86400000L * 2,
            dateFormatted = "21/09/2026 - 08:30",
            studentName = "Maria Silva",
            isCleared = true,
            reportedSymptoms = emptyList(),
            bloodPressure = "120/80 mmHg",
            heartRate = "72 bpm",
            studentNotes = "Me sentindo ótima e bem disposta.",
            recommendationText = "Treino liberado normalmente com hidratação periódica."
        ),
        SafetyCheckRecord(
            id = "rec_002",
            timestamp = System.currentTimeMillis() - 86400000L * 5,
            dateFormatted = "18/09/2026 - 09:15",
            studentName = "Maria Silva",
            isCleared = false,
            reportedSymptoms = listOf("Tontura"),
            bloodPressure = "105/65 mmHg",
            heartRate = "68 bpm",
            studentNotes = "Acordei com leve tontura ao levantar da cama.",
            recommendationText = "Sessão suspensa preventivamente. Aluna orientada a beber água e repousar."
        )
    )
}
