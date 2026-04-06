package com.plantguide.data.database

import com.plantguide.data.entity.Plant

object PlantDataSource {

    fun getDefaultPlants(): List<Plant> = listOf(

        Plant(
            name = "Espada-de-São-Jorge",
            scientificName = "Sansevieria trifasciata",
            imageResName = "plant_sansevieria",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Snake_Plant_%28Sansevieria_trifasciata_%27Laurentii%27%29.jpg/800px-Snake_Plant_%28Sansevieria_trifasciata_%27Laurentii%27%29.jpg",
            lightLevel = "Baixa a Alta — extremamente adaptável, suporta sombra e pleno sol",
            wateringFrequency = "A cada 15–30 dias; deixe o substrato secar completamente entre regas",
            idealEnvironment = "Interno — sala, quarto, escritório; tolera ambientes com pouca ventilação",
            basicCare = "Use substrato bem drenado. Evite excesso de água, pois apodrece as raízes facilmente. Limpe as folhas com pano úmido para remover poeira. Retire folhas secas ou amareladas. Não precisa de adubação frequente — uma vez por semestre na primavera/verão.",
            isToxicForPets = true,
            category = "Suculenta"
        ),

        Plant(
            name = "Pothos",
            scientificName = "Epipremnum aureum",
            imageResName = "plant_pothos",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/ab/Epipremnum_aureum_31082012.jpg/800px-Epipremnum_aureum_31082012.jpg",
            lightLevel = "Baixa a Média — ideal em locais com luz indireta; evite sol direto intenso",
            wateringFrequency = "A cada 7–10 dias; aguarde o solo secar levemente entre regas",
            idealEnvironment = "Interno — banheiros, cozinhas, escritórios; excelente para ambientes úmidos",
            basicCare = "Uma das plantas mais fáceis de cultivar. Pode ser cultivada em água ou terra. Apare os galhos longos para estimular crescimento mais denso. Adube mensalmente durante primavera e verão. Suporta propagação por estacas facilmente.",
            isToxicForPets = true,
            category = "Trepadeira"
        ),

        Plant(
            name = "Orquídea Phalaenopsis",
            scientificName = "Phalaenopsis spp.",
            imageResName = "plant_orchid",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/5/5e/Orchid_Phalaenopsis.jpg/800px-Orchid_Phalaenopsis.jpg",
            lightLevel = "Média — luz indireta brilhante; janelas voltadas para leste ou oeste são ideais",
            wateringFrequency = "A cada 7 dias; mergulhe o vaso em água por 15 min e escorra bem",
            idealEnvironment = "Interno — ambientes com boa circulação de ar e umidade moderada",
            basicCare = "Use substrato específico para orquídeas (casca de pinus). Após a floração, corte a haste acima do segundo nó para estimular nova florada. Adube quinzenalmente com fertilizante para orquídeas durante o período de crescimento. Não deixe água acumular no centro das folhas.",
            isToxicForPets = false,
            category = "Flor"
        ),

        Plant(
            name = "Suculenta Echeveria",
            scientificName = "Echeveria spp.",
            imageResName = "plant_echeveria",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Echeveria_lola2.jpg/800px-Echeveria_lola2.jpg",
            lightLevel = "Alta — necessita de pelo menos 6 horas de sol direto por dia",
            wateringFrequency = "A cada 14–21 dias; o substrato deve estar completamente seco antes de regar",
            idealEnvironment = "Externo — varandas, jardins, peitoris ensolarados; também aceita ambientes internos muito iluminados",
            basicCare = "Nunca deixe água empoçada no prato ou na roseta central — causa podridão. Use substrato arenoso e bem drenado. No inverno, reduza a rega à metade. Propaga-se facilmente por folhas ou filhotes. Não precisa de adubação frequente.",
            isToxicForPets = false,
            category = "Suculenta"
        ),

        Plant(
            name = "Ficus Lyrata",
            scientificName = "Ficus lyrata",
            imageResName = "plant_ficus",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d0/Ficus_lyrata_2019.jpg/800px-Ficus_lyrata_2019.jpg",
            lightLevel = "Alta — necessita de luz brilhante indireta; evite mudanças bruscas de posição",
            wateringFrequency = "A cada 7–10 dias; verifique os 3 cm superiores do solo antes de regar",
            idealEnvironment = "Interno — sala de estar com boa iluminação natural; longe de correntes de ar frio",
            basicCare = "Não mude a planta de lugar com frequência — ela perde folhas com o estresse. Limpe as folhas grandes regularmente. Adube mensalmente na primavera e verão. Use vaso com boa drenagem. Verifique periodicamente a presença de cochonilhas e ácaros.",
            isToxicForPets = true,
            category = "Arbustiva"
        ),

        Plant(
            name = "Cacto-de-Natal",
            scientificName = "Schlumbergera truncata",
            imageResName = "plant_christmas_cactus",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a3/Schlumbergera_truncata2.jpg/800px-Schlumbergera_truncata2.jpg",
            lightLevel = "Média — luz indireta brilhante; evite sol direto que queima os segmentos",
            wateringFrequency = "A cada 10–14 dias durante o crescimento; reduzir no outono para induzir floração",
            idealEnvironment = "Interno — ambientes frescos e úmidos; varanda coberta ou sala",
            basicCare = "Para induzir floração no Natal, exponha a planta a noites mais frias (15°C) e reduza a rega em outubro. Após a floração, deixe descansar por 4–6 semanas. Adube quinzenalmente durante a primavera e o verão com fertilizante balanceado.",
            isToxicForPets = false,
            category = "Cacto"
        ),

        Plant(
            name = "Costela-de-Adão",
            scientificName = "Monstera deliciosa",
            imageResName = "plant_monstera",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/f9/Monstera_deliciosa2.jpg/800px-Monstera_deliciosa2.jpg",
            lightLevel = "Média — luz indireta brilhante; folhas sem furos indicam pouca luz",
            wateringFrequency = "A cada 7–10 dias; deixe o topo do solo secar entre regas",
            idealEnvironment = "Interno — salas amplas e bem iluminadas; cresce rapidamente e precisa de espaço",
            basicCare = "Forneça um tutor ou suporte para o caule à medida que cresce. Borrife as folhas com água para aumentar a umidade. Adube mensalmente na época de crescimento. Pode ser podada para controlar o tamanho. Propaga-se por estacas com facilidade.",
            isToxicForPets = true,
            category = "Tropical"
        ),

        Plant(
            name = "Lavanda",
            scientificName = "Lavandula angustifolia",
            imageResName = "plant_lavender",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/7/7a/Lavendel_in_der_Provence.jpg/800px-Lavendel_in_der_Provence.jpg",
            lightLevel = "Alta — exige pleno sol, pelo menos 6–8 horas por dia",
            wateringFrequency = "A cada 10–15 dias; altamente tolerante à seca; não regue em excesso",
            idealEnvironment = "Externo — jardins, hortas, varandas abertas com muito sol; não se adapta bem a ambientes fechados",
            basicCare = "Poda após a floração para manter a forma compacta. Use substrato alcalino e bem drenado. Evite regar as flores e folhas — regue apenas na base. Muito resistente ao calor e à seca. Repele insetos naturalmente. Floresce na primavera e no verão.",
            isToxicForPets = true,
            category = "Aromática"
        ),

        Plant(
            name = "Zamioculca",
            scientificName = "Zamioculcas zamiifolia",
            imageResName = "plant_zamioculca",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Zamioculcas_zamiifolia1.jpg/800px-Zamioculcas_zamiifolia1.jpg",
            lightLevel = "Baixa a Média — uma das plantas mais tolerantes à sombra; evite sol direto",
            wateringFrequency = "A cada 15–20 dias; armazena água nos rizomas, muito tolerante ao esquecimento",
            idealEnvironment = "Interno — corredores, quartos, escritórios com pouca luz natural; ideal para iniciantes",
            basicCare = "Nunca deixe encharcada — os rizomas apodrecem facilmente. Use substrato bem drenado. Limpe as folhas brilhantes com pano úmido. Não precisa de adubação frequente — a cada 3 meses é suficiente. Cresce lentamente, mas é extremamente resistente.",
            isToxicForPets = true,
            category = "Tropical"
        ),

        Plant(
            name = "Rosa",
            scientificName = "Rosa spp.",
            imageResName = "plant_rose",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Rosa_rubiginosa_1.jpg/800px-Rosa_rubiginosa_1.jpg",
            lightLevel = "Alta — necessita de pelo menos 6 horas de sol direto para florescer bem",
            wateringFrequency = "A cada 2–3 dias no verão; a cada 5–7 dias no inverno; regue na base",
            idealEnvironment = "Externo — jardins, vasos grandes em varandas com pleno sol; não tolera ambientes fechados",
            basicCare = "Poda regularmente após a floração para estimular novas flores. Adube a cada 15 dias com fertilizante rico em potássio durante o florescimento. Fique atento a doenças fúngicas como oídio e ferrugem — trate com fungicida adequado. Retire flores murchas para estimular novas brotações.",
            isToxicForPets = false,
            category = "Flor"
        ),

        Plant(
            name = "Aloe Vera",
            scientificName = "Aloe barbadensis miller",
            imageResName = "plant_aloe",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4b/Aloe_vera_flower_inset.png/800px-Aloe_vera_flower_inset.png",
            lightLevel = "Alta — prefere pleno sol ou luz indireta muito intensa",
            wateringFrequency = "A cada 14–21 dias; deixe o solo secar completamente entre regas",
            idealEnvironment = "Interno ou Externo — peitoris ensolarados, varandas; tolera bem ambientes secos",
            basicCare = "Use substrato para cactos e suculentas. Nunca deixe a planta em prato com água acumulada. O gel das folhas tem propriedades medicinais para queimaduras leves e irritações de pele. Retire as folhas externas mais velhas para uso. Propaga-se facilmente por filhotes.",
            isToxicForPets = true,
            category = "Suculenta"
        ),

        Plant(
            name = "Samambaio",
            scientificName = "Nephrolepis exaltata",
            imageResName = "plant_fern",
            imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e5/Nephrolepis_exaltata_RBGV.jpg/800px-Nephrolepis_exaltata_RBGV.jpg",
            lightLevel = "Baixa a Média — luz indireta suave; evite sol direto que resseca as folhas",
            wateringFrequency = "A cada 2–3 dias; gosta de solo sempre levemente úmido e alta umidade do ar",
            idealEnvironment = "Interno — banheiros, cozinhas, varandas cobertas; adora ambientes úmidos e com boa circulação de ar",
            basicCare = "Borrife as folhas diariamente ou use umidificador próximo. Nunca deixe o solo secar completamente. Retire folhas amarelas ou secas regularmente. Adube mensalmente durante a primavera e o verão. Repote anualmente pois cresce rápido e se adapta bem a cestas suspensas.",
            isToxicForPets = false,
            category = "Samambaia"
        )
    )
}
