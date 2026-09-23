package com.example.data

object ConditionWorkoutsData {

    val protocols: List<ConditionWorkoutProtocol> = listOf(
        // 1. Hipertensão (força, mobilidade, funcional, equilíbrio)
        ConditionWorkoutProtocol(
            id = "hipertensao",
            conditionName = "Hipertensão",
            title = "Protocolo Cardiovascular & Controle Pressórico",
            emoji = "❤️",
            pillars = listOf("força", "mobilidade", "funcional", "equilíbrio"),
            clinicalRationale = "Treinamento resistido dinâmico com cargas moderadas reduz a pressão arterial em repouso (efeito hipotensor pós-exercício). A ênfase é na respiração fluida e no controle postural.",
            precautions = "Evitar manobra de Valsalva (nunca prender a respiração). Pausas ativas com hidratação. Manter percepção de esforço na faixa moderada (Borg 4-6).",
            targetAudience = "Idosos hipertensos compensados em uso de anti-hipertensivos.",
            estimatedDurationMinutes = 35,
            exercises = listOf(
                DirectedExercise(
                    id = "hip_1",
                    name = "Mobilidade Cervical e Escapular",
                    pillar = "mobilidade",
                    sets = 2,
                    repsOrTime = "8 rotações lentas",
                    restSeconds = 45,
                    instruction = "Gire os ombros para trás e incline suavemente o pescoço de um lado ao outro.",
                    clinicalGuideline = "Alivia a tensão simpática e estabiliza a frequência cardíaca inicial.",
                    emoji = "🔄"
                ),
                DirectedExercise(
                    id = "hip_2",
                    name = "Agachamento Guiado na Cadeira com Expiração Contínua",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "10 repetições",
                    restSeconds = 60,
                    instruction = "Inspire ao descer em direção à cadeira e solte o ar pela boca ao subir.",
                    clinicalGuideline = "A expiração na fase concêntrica previne picos de pressão arterial sistólica.",
                    emoji = "🪑"
                ),
                DirectedExercise(
                    id = "hip_3",
                    name = "Sentar, Levantar e Alcançar Caixa Acima",
                    pillar = "funcional",
                    sets = 3,
                    repsOrTime = "8 repetições",
                    restSeconds = 60,
                    instruction = "Levante da cadeira e eleve suavemente uma almofada ou toalha à frente.",
                    clinicalGuideline = "Simula tarefas domésticas de autonomia diária sem sobrecarga de membros superiores.",
                    emoji = "📦"
                ),
                DirectedExercise(
                    id = "hip_4",
                    name = "Apoio Unipodal com Barra de Segurança",
                    pillar = "equilíbrio",
                    sets = 3,
                    repsOrTime = "15 segundos cada perna",
                    restSeconds = 45,
                    instruction = "Fique em um pé só mantendo uma das mãos levemente apoiada no espaldar da cadeira.",
                    clinicalGuideline = "Previne quedas e fortalece estabilizadores de tornozelo sem elevação súbita de PA.",
                    emoji = "⚖️"
                )
            )
        ),

        // 2. Diabetes tipo 2 (força, funcional, caminhada, equilíbrio)
        ConditionWorkoutProtocol(
            id = "diabetes_tipo_2",
            conditionName = "Diabetes tipo 2",
            title = "Protocolo de Captação Glicêmica & Metabolismo",
            emoji = "🩸",
            pillars = listOf("força", "funcional", "caminhada", "equilíbrio"),
            clinicalRationale = "O estímulo muscular dos grandes grupos potencializa a translocação de GLUT-4 independente de insulina, otimizando o controle glicêmico e reduzindo a resistência insulínica.",
            precautions = "Atenção a sinais de hipoglicemia (tontura, suor frio). Usar calçados confortáveis e inspecionar os pés (cuidado com neuropatia periférica). Ter carboidrato simples acessível.",
            targetAudience = "Idosos com diabetes tipo 2 com glicemias controladas.",
            estimatedDurationMinutes = 40,
            exercises = listOf(
                DirectedExercise(
                    id = "dia_1",
                    name = "Leg Press Adaptado na Faixa Elástica",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "12 repetições",
                    restSeconds = 60,
                    instruction = "Empurre o elástico com os pés ativando quadríceps e glúteos de forma cadenciada.",
                    clinicalGuideline = "Grandes massas musculares consomem mais glicose circulante pós-treino.",
                    emoji = "🦵"
                ),
                DirectedExercise(
                    id = "dia_2",
                    name = "Remada Sentada com Elástico",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "12 repetições",
                    restSeconds = 60,
                    instruction = "Puxe os cotovelos para trás aproximando as escápulas.",
                    clinicalGuideline = "Ativa dorsais e trapézio médio, melhorando gasto energético global.",
                    emoji = "🚣"
                ),
                DirectedExercise(
                    id = "dia_3",
                    name = "Circuito Funcional de Passos e Transferência de Objetos",
                    pillar = "funcional",
                    sets = 3,
                    repsOrTime = "10 passos com transferência",
                    restSeconds = 60,
                    instruction = "Caminhe pegando um objeto em uma mesa e posicionando em outra à frente.",
                    clinicalGuideline = "Coordena demandas cognitivo-motoras e estimula a autonomia residencial.",
                    emoji = "🧺"
                ),
                DirectedExercise(
                    id = "dia_4",
                    name = "Caminhada Contínua com Cadência Ritmo Seguro",
                    pillar = "caminhada",
                    sets = 1,
                    repsOrTime = "10 a 12 minutos",
                    restSeconds = 90,
                    instruction = "Caminhe em piso plano e antiderrapante mantendo passos firmes e respiração nasal/bucal.",
                    clinicalGuideline = "Melhora a circulação periférica e mantém oxigenação tecidual estável.",
                    emoji = "🚶"
                ),
                DirectedExercise(
                    id = "dia_5",
                    name = "Equilíbrio em Posição Tandem (Pé ante Pé)",
                    pillar = "equilíbrio",
                    sets = 3,
                    repsOrTime = "15 segundos cada lado",
                    restSeconds = 45,
                    instruction = "Coloque o calcanhar de um pé encostado nos dedos do outro perto de um apoio.",
                    clinicalGuideline = "Estimula propiocepção e compensa eventuais perdas sensitivas nos pés.",
                    emoji = "🎯"
                )
            )
        ),

        // 3. Osteoporose (força, postura, equilíbrio, funcionalidade)
        ConditionWorkoutProtocol(
            id = "osteoporose",
            conditionName = "Osteoporose",
            title = "Protocolo de Densidade Óssea & Proteção Vertebral",
            emoji = "🦴",
            pillars = listOf("força", "postura", "equilíbrio", "funcionalidade"),
            clinicalRationale = "Cargas compressivas axiais suaves estimulam a osteogênese mecânica. Foco essencial na extensão torácica e no combate à hipercifose senil, protegendo vértebras.",
            precautions = "Contraindicada flexão abrupta da coluna lombar ou torções vigorosas do tronco. Movimentos lentos e controlados.",
            targetAudience = "Idosos diagnosticados com osteopenia ou osteoporose na coluna ou fêmur.",
            estimatedDurationMinutes = 35,
            exercises = listOf(
                DirectedExercise(
                    id = "ost_1",
                    name = "Extensão de Joelhos Sentado com Tornozeleira Leve",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "10 repetições",
                    restSeconds = 60,
                    instruction = "Estenda a perna mantendo as costas bem apoiadas na cadeira, segurando 1s no topo.",
                    clinicalGuideline = "Estímulo tensional no tendão patelar e fêmur distal com segurança articular.",
                    emoji = "🦵"
                ),
                DirectedExercise(
                    id = "ost_2",
                    name = "Retração Escapular e Abertura Peitoral (Postura)",
                    pillar = "postura",
                    sets = 3,
                    repsOrTime = "10 repetições mantendo 3s",
                    restSeconds = 45,
                    instruction = "Abra os braços em 'W' abrindo o peito sem arquear a região lombar.",
                    clinicalGuideline = "Combate a cifose dorsal senil e reduz a sobrecarga sobre os corpos vertebrais.",
                    emoji = "📐"
                ),
                DirectedExercise(
                    id = "ost_3",
                    name = "Apoio Semitandem com Estabilização Visual",
                    pillar = "equilíbrio",
                    sets = 3,
                    repsOrTime = "20 segundos cada perna",
                    restSeconds = 45,
                    instruction = "Posicione os pés desalinhados lateralmente, focando um ponto fixo à frente.",
                    clinicalGuideline = "Reduz vertigem postural e fortalece estabilizadores pélvicos.",
                    emoji = "👁️"
                ),
                DirectedExercise(
                    id = "ost_4",
                    name = "Alcance Funcional em Diferentes Alturas",
                    pillar = "funcionalidade",
                    sets = 3,
                    repsOrTime = "8 alcances seguros",
                    restSeconds = 60,
                    instruction = "Alcance um objeto imaginário na altura do peito e dos ombros sem inclinar a coluna.",
                    clinicalGuideline = "Treina mecânica corporal protetora para pegar utensílios no dia a dia.",
                    emoji = "🤲"
                )
            )
        ),

        // 4. Osteoartrite (mobilidade, força, funcional, controle de impacto)
        ConditionWorkoutProtocol(
            id = "osteoartrite",
            conditionName = "Osteoartrite",
            title = "Protocolo Articular Suave & Lubrificação Sinovial",
            emoji = "🦵",
            pillars = listOf("mobilidade", "força", "funcional", "controle de impacto"),
            clinicalRationale = "O movimento de baixa carga nutre e lubrifica a cartilagem articular pelo fluido sinovial. O fortalecimento muscular periarticular dissipa forças de impacto sobre o osso subcondral.",
            precautions = "Nunca exercitar até o ponto de dor inflamatória aguda. Sem saltos, impactos repetitivos ou amplitudes forçadas.",
            targetAudience = "Idosos com gonartrose (joelhos), coxartrose (quadril) ou artrose nas mãos.",
            estimatedDurationMinutes = 30,
            exercises = listOf(
                DirectedExercise(
                    id = "art_1",
                    name = "Mobilidade de Quadril e Tornozelo em Pêndulo",
                    pillar = "mobilidade",
                    sets = 2,
                    repsOrTime = "10 balanços suaves",
                    restSeconds = 45,
                    instruction = "Apoiado em uma barra, balance a perna suavemente para frente e para trás.",
                    clinicalGuideline = "Aumenta a circulação do líquido sinovial na cápsula do quadril.",
                    emoji = "🌊"
                ),
                DirectedExercise(
                    id = "art_2",
                    name = "Isometria Suave de Quadríceps na Almofada",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "8 segundos de sustentação",
                    restSeconds = 60,
                    instruction = "Pressione a almofada sob o joelho contra a maca ou cadeira sem dobrar demais a articulação.",
                    clinicalGuideline = "Fortalece o vasto medial sem atrito patelofemoral nocivo.",
                    emoji = "🛡️"
                ),
                DirectedExercise(
                    id = "art_3",
                    name = "Passos Laterais sem Impacto com Mini Faixa Leve",
                    pillar = "controle de impacto",
                    sets = 3,
                    repsOrTime = "8 passos para cada lado",
                    restSeconds = 60,
                    instruction = "Dê passos laterais suaves, mantendo joelhos semi-flexionados e aterrissagem silenciosa.",
                    clinicalGuideline = "Dissipa a força de reação do solo através do glúteo médio.",
                    emoji = "🦶"
                ),
                DirectedExercise(
                    id = "art_4",
                    name = "Elevação Pélvica (Ponte) Funcional no Colchonete ou Cama",
                    pillar = "funcional",
                    sets = 3,
                    repsOrTime = "10 repetições",
                    restSeconds = 60,
                    instruction = "Deitado com joelhos dobrados, eleve o quadril acionando glúteos.",
                    clinicalGuideline = "Treina a força de transferência necessária para se mover na cama e levantar.",
                    emoji = "🌉"
                )
            )
        ),

        // 5. Parkinson (mobilidade, amplitude de movimento, força, marcha, equilíbrio, dupla tarefa)
        ConditionWorkoutProtocol(
            id = "parkinson",
            conditionName = "Parkinson",
            title = "Protocolo Neuro-Motor & Estímulos Rítmicos",
            emoji = "🧠",
            pillars = listOf("mobilidade", "amplitude de movimento", "força", "marcha", "equilíbrio", "dupla tarefa"),
            clinicalRationale = "Pistas auditivas e visuais com movimentos voluntários amplos combatem a bradicinesia, a rigidez e os episódios de congelamento (freezing). Dupla tarefa estimula a neuroplasticidade basal.",
            precautions = "Ambiente totalmente livre de obstáculos no chão. Sempre contar com barra ou profissional ao lado durante as tarefas de marcha.",
            targetAudience = "Idosos diagnosticados com Doença de Parkinson nos estágios 1 a 3 da escala Hoehn & Yahr.",
            estimatedDurationMinutes = 40,
            exercises = listOf(
                DirectedExercise(
                    id = "prk_1",
                    name = "Mobilidade com Rotação Axial do Tronco",
                    pillar = "mobilidade",
                    sets = 2,
                    repsOrTime = "8 giros suaves",
                    restSeconds = 45,
                    instruction = "Sentado com bastão nos ombros, gire suavemente o tronco olhando para cada lado.",
                    clinicalGuideline = "Combate a rigidez axial típica do Parkinson e facilita viradas de cama.",
                    emoji = "🥢"
                ),
                DirectedExercise(
                    id = "prk_2",
                    name = "Passos Amplos com Pistas no Solo (Big Movements)",
                    pillar = "amplitude de movimento",
                    sets = 3,
                    repsOrTime = "10 passos ampliados",
                    restSeconds = 60,
                    instruction = "Dê passos grandes ultrapassando faixas marcadas no chão, abrindo os braços.",
                    clinicalGuideline = "Reeduca o tamanho do passo afetado pela hipometria senil e parkinsoniana.",
                    emoji = "📏"
                ),
                DirectedExercise(
                    id = "prk_3",
                    name = "Levantar da Cadeira com Extensão Máxima",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "8 repetições",
                    restSeconds = 60,
                    instruction = "Incline o tronco para a frente e levante com ímpeto controlado, abrindo o peito no topo.",
                    clinicalGuideline = "Reforça extensores de quadril essenciais contra a postura fletida em flexão.",
                    emoji = "⬆️"
                ),
                DirectedExercise(
                    id = "prk_4",
                    name = "Marcha com Pista Sonora Rítmica (Metrônomo 1-2)",
                    pillar = "marcha",
                    sets = 3,
                    repsOrTime = "1 minuto de marcha",
                    restSeconds = 60,
                    instruction = "Caminhe no ritmo do comando 'UM - DOIS - UM - DOIS' com elevação nítida dos joelhos.",
                    clinicalGuideline = "A pista auditiva contorna os circuitos dos gânglios da base deficientes.",
                    emoji = "🎵"
                ),
                DirectedExercise(
                    id = "prk_5",
                    name = "Equilíbrio Bipodal com Oscilação e Olhos Fechados",
                    pillar = "equilíbrio",
                    sets = 3,
                    repsOrTime = "15 segundos",
                    restSeconds = 45,
                    instruction = "Pés juntos perto do apoio, feche os olhos e sinta a pressão nos calcanhares.",
                    clinicalGuideline = "Reativa sistemas vestibular e somatossensorial para evitar quedas.",
                    emoji = "🧘"
                ),
                DirectedExercise(
                    id = "prk_6",
                    name = "Marcha com Dupla Tarefa Cognitiva (Dias da Semana / Cores)",
                    pillar = "dupla tarefa",
                    sets = 2,
                    repsOrTime = "1 minuto por bloco",
                    restSeconds = 60,
                    instruction = "Caminhe falando os meses do ano de trás para frente ou cores de objetos.",
                    clinicalGuideline = "Treina a atenção dividida para prevenir congelamento de marcha em ambientes reais.",
                    emoji = "🧩"
                )
            )
        ),

        // 6. Pós-AVC (mobilidade, força, equilíbrio, marcha, funcionalidade)
        ConditionWorkoutProtocol(
            id = "pos_avc",
            conditionName = "Pós-AVC",
            title = "Protocolo de Reabilitação Neuro-Funcional & Simetria",
            emoji = "⚡",
            pillars = listOf("mobilidade", "força", "equilíbrio", "marcha", "funcionalidade"),
            clinicalRationale = "Treinamento voltado para a descarga de peso no dimídio plégico/parético, promovendo reaprendizado motor, simetria postural e prevenção de padrão espástico compensatório.",
            precautions = "Garantir assistência direta no lado acometido. Respeitar a fadiga neurológica central. Apoio seguro de espaldar ou barras paralelas.",
            targetAudience = "Idosos em fase subaguda ou crônica de recuperação após acidente vascular cerebral.",
            estimatedDurationMinutes = 40,
            exercises = listOf(
                DirectedExercise(
                    id = "avc_1",
                    name = "Mobilização Passiva-Assistida do Membro Parestésico",
                    pillar = "mobilidade",
                    sets = 2,
                    repsOrTime = "10 repetições",
                    restSeconds = 45,
                    instruction = "Utilize o braço saudável para guiar a elevação do outro braço à frente.",
                    clinicalGuideline = "Evita contraturas em padrão flexor e preserva a integridade do ombro.",
                    emoji = "🤝"
                ),
                DirectedExercise(
                    id = "avc_2",
                    name = "Descarga de Peso com Apoio Simétrico",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "8 transferências lentas",
                    restSeconds = 60,
                    instruction = "Em pé, transfira o peso do corpo para o lado mais fraco, mantendo o joelho alinhado.",
                    clinicalGuideline = "Reativa receptores de Ruffini e propriocepção mecânica no membro afetado.",
                    emoji = "⚖️"
                ),
                DirectedExercise(
                    id = "avc_3",
                    name = "Equilíbrio Estático com Deslocamento Lateral do Tronco",
                    pillar = "equilíbrio",
                    sets = 3,
                    repsOrTime = "15 segundos cada lado",
                    restSeconds = 45,
                    instruction = "Com apoio das mãos, desloque o quadril suavemente à esquerda e à direita.",
                    clinicalGuideline = "Recupera o controle postural antecipatório e as reações de endireitamento.",
                    emoji = "🧭"
                ),
                DirectedExercise(
                    id = "avc_4",
                    name = "Treino de Passo com Elevação e Apoio de Calcanhar",
                    pillar = "marcha",
                    sets = 3,
                    repsOrTime = "8 passos assistidos",
                    restSeconds = 60,
                    instruction = "Foque em encostar o calcanhar do pé acometido primeiro, sem arrastar a ponta.",
                    clinicalGuideline = "Inibe a marcha ceifante e facilita a fase de balanço da perna.",
                    emoji = "👟"
                ),
                DirectedExercise(
                    id = "avc_5",
                    name = "Sentar e Levantar com Carga nos Dois Calcanhares",
                    pillar = "funcionalidade",
                    sets = 3,
                    repsOrTime = "8 repetições",
                    restSeconds = 60,
                    instruction = "Levante da cadeira garantindo que os dois pés façam força igual contra o chão.",
                    clinicalGuideline = "Restaura o padrão bilateral simétrico para transferências diárias.",
                    emoji = "🪑"
                )
            )
        ),

        // 7. Sarcopenia (força, potência adaptada, equilíbrio, funcionalidade)
        ConditionWorkoutProtocol(
            id = "sarcopenia",
            conditionName = "Sarcopenia",
            title = "Protocolo de Hipertrofia Geriátrica & Taxa de Força",
            emoji = "💪",
            pillars = listOf("força", "potência adaptada", "equilíbrio", "funcionalidade"),
            clinicalRationale = "O envelhecimento causa atrofia seletiva de fibras tipo II (rápidas). O treinamento combinando força com fase concêntrica rápida (potência) restaura a velocidade de reação e a massa muscular.",
            precautions = "Aumento progressivo de volume. Acompanhar aporte proteico orientado por nutricionista. Intervalos de recuperação completos (60 a 90 segundos).",
            targetAudience = "Idosos com baixa massa muscular, perda de velocidade de marcha ou força de preensão manual diminuída.",
            estimatedDurationMinutes = 40,
            exercises = listOf(
                DirectedExercise(
                    id = "sar_1",
                    name = "Agachamento com Fase Concêntrica Dinâmica (Potência)",
                    pillar = "potência adaptada",
                    sets = 3,
                    repsOrTime = "8 repetições vigorosas",
                    restSeconds = 75,
                    instruction = "Desça em 3 segundos e suba o mais rápido que puder com segurança e controle.",
                    clinicalGuideline = "Recruta unidades motoras de alto limiar (fibras musculares tipo II).",
                    emoji = "⚡"
                ),
                DirectedExercise(
                    id = "sar_2",
                    name = "Supino com Halteres / Faixa Elástica Forte",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "10 repetições",
                    restSeconds = 60,
                    instruction = "Empurre os pesos para cima soltando o ar e contraindo os peitorais.",
                    clinicalGuideline = "Combate a perda de massa magra nos membros superiores e cintura escapular.",
                    emoji = "🏋️"
                ),
                DirectedExercise(
                    id = "sar_3",
                    name = "Elevação de Panturrilhas em Degrau com Apoio",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "12 repetições",
                    restSeconds = 60,
                    instruction = "Suba na ponta dos pés o máximo possível, segurando 1s no pico da contração.",
                    clinicalGuideline = "Fortalece tríceps sural, motor primordial da propulsão durante a marcha.",
                    emoji = "🦶"
                ),
                DirectedExercise(
                    id = "sar_4",
                    name = "Deslocamento Unipodal com Paradas Bruscas",
                    pillar = "equilíbrio",
                    sets = 3,
                    repsOrTime = "6 paradas cada perna",
                    restSeconds = 60,
                    instruction = "Dê um passo à frente e congele o movimento em apoio unipodal por 2 segundos.",
                    clinicalGuideline = "Treina desaceleração e reações de equilíbrio ante tropeços repentinos.",
                    emoji = "🛑"
                ),
                DirectedExercise(
                    id = "sar_5",
                    name = "Transporte de Cargas Leves (Farmer's Walk Seguro)",
                    pillar = "funcionalidade",
                    sets = 3,
                    repsOrTime = "15 passos com carga",
                    restSeconds = 60,
                    instruction = "Caminhe com postura ereta segurando 1 ou 2 garrafas ou pesos leves nas mãos.",
                    clinicalGuideline = "Aumenta a força de preensão manual e estabilidade da cintura pélvica.",
                    emoji = "🛍️"
                )
            )
        ),

        // 8. Fragilidade (exercícios de baixa complexidade, força, mobilidade, equilíbrio, atividades funcionais)
        ConditionWorkoutProtocol(
            id = "fragilidade",
            conditionName = "Fragilidade",
            title = "Protocolo de Preservação Autônoma & Baixo Impacto",
            emoji = "🌱",
            pillars = listOf("exercícios de baixa complexidade", "força", "mobilidade", "equilíbrio", "atividades funcionais"),
            clinicalRationale = "Para o idoso com síndrome da fragilidade ou pré-fragilidade, os estímulos devem ser fracionados, sem fadiga excessiva, focados em preservar a independência em atividades da vida diária.",
            precautions = "Monitoramento constante de cansaço extremo ou sudorese. Sessões com pausas frequentes. Realização de quase todos os exercícios sentados ou com apoio duplo.",
            targetAudience = "Idosos frágeis, pós-hospitalização ou com fadiga crônica e perda de peso não intencional.",
            estimatedDurationMinutes = 25,
            exercises = listOf(
                DirectedExercise(
                    id = "frg_1",
                    name = "Bicicleta no Ar Sentado em Cadeira Firme",
                    pillar = "exercícios de baixa complexidade",
                    sets = 2,
                    repsOrTime = "8 rotações alternadas",
                    restSeconds = 60,
                    instruction = "Sentado confortavelmente, mova as pernas como se pedalasse suavemente no ar.",
                    clinicalGuideline = "Exercício simples que ativa a circulação venosa sem sobrecarga de equilíbrio.",
                    emoji = "🚲"
                ),
                DirectedExercise(
                    id = "frg_2",
                    name = "Aperto de Bolinha Macia e Extensão dos Dedos",
                    pillar = "força",
                    sets = 3,
                    repsOrTime = "10 apertos por mão",
                    restSeconds = 45,
                    instruction = "Aperte a bolinha de espuma por 2 segundos e solte abrindo os dedos completamente.",
                    clinicalGuideline = "Melhora a força de preensão para abrir potes, torneiras e chaves.",
                    emoji = "🎾"
                ),
                DirectedExercise(
                    id = "frg_3",
                    name = "Mobilidade de Tornozelos 'Ponta e Calcanhar' Sentado",
                    pillar = "mobilidade",
                    sets = 2,
                    repsOrTime = "12 repetições",
                    restSeconds = 45,
                    instruction = "Sentado, levante as pontas dos pés e depois levante os calcanhares alternadamente.",
                    clinicalGuideline = "Estimula a bomba da panturrilha e previne inchaço (edema) nos membros inferiores.",
                    emoji = "🦶"
                ),
                DirectedExercise(
                    id = "frg_4",
                    name = "Equilíbrio Sentado com Inclinação Controlada do Tronco",
                    pillar = "equilíbrio",
                    sets = 2,
                    repsOrTime = "6 inclinações suaves",
                    restSeconds = 45,
                    instruction = "Sentado ereto, incline levemente o corpo para frente e para trás, sentindo o abdômen.",
                    clinicalGuideline = "Fortalece a musculatura central (core) essencial para não tombar na cadeira.",
                    emoji = "🪑"
                ),
                DirectedExercise(
                    id = "frg_5",
                    name = "Simulação de Vestir Roupa e Abotoar Camisa",
                    pillar = "atividades funcionais",
                    sets = 2,
                    repsOrTime = "8 movimentos coordenados",
                    restSeconds = 60,
                    instruction = "Mova os braços como se colocasse um casaco, treinando alcance e pinça fina.",
                    clinicalGuideline = "Foco total na auto-estima e na manutenção do autocuidado diário sem dependência.",
                    emoji = "👕"
                )
            )
        )
    )
}
