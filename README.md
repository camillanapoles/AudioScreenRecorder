# AudioScreenRecorder

Uma aplicação Android para gravar áudio do sistema e tela, compatível com Android 15 e Nothing Phone 2.

## Funcionalidades

- ✅ Gravação de áudio do sistema
- ✅ Gravação de áudio e tela simultaneamente
- ✅ Seleção de modo de gravação
- ✅ Contagem regressiva configurável (padrão: 3 segundos)
- ✅ Pasta de salvamento personalizável
- ✅ Múltiplos formatos de áudio (MP4, AAC, 3GP)
- ✅ Interface simples e intuitiva
- ✅ CI/CD com GitHub Actions

## Requisitos

- Android 8.0 (API 26) ou superior
- Testado no Android 15
- Testado no Nothing Phone 2

## Permissões Necessárias

- `RECORD_AUDIO` - Para gravar áudio
- `FOREGROUND_SERVICE` - Para executar gravação em segundo plano
- `FOREGROUND_SERVICE_MEDIA_PROJECTION` - Para gravação de tela
- `POST_NOTIFICATIONS` - Para notificações (Android 13+)
- Armazenamento (versões antigas do Android)

## Instalação

### Via APK
1. Baixe o APK mais recente da seção Releases
2. Habilite "Fontes desconhecidas" nas configurações do Android
3. Instale o APK

### Via código-fonte
1. Clone o repositório
2. Abra no Android Studio
3. Execute ou compile o projeto

## Uso

1. **Inicie o aplicativo**
2. **Conceda as permissões necessárias**
3. **Configure suas preferências** (opcional):
   - Modo de gravação (apenas áudio ou áudio + tela)
   - Tempo de contagem regressiva
   - Pasta de salvamento
   - Formato de áudio
4. **Toque em "Iniciar Gravação"**
5. **Aguarde a contagem regressiva**
6. **Para gravação de tela**, aceite a permissão MediaProjection
7. **Toque em "Parar Gravação"** quando terminar
8. **Encontre sua gravação** na pasta configurada

## Configurações

### Modo de Gravação
- **Apenas Áudio**: Grava apenas o áudio do dispositivo
- **Áudio e Tela**: Grava áudio e captura de tela simultaneamente

### Tempo de Contagem Regressiva
- Ajustável de 1 a 10 segundos (padrão: 3)
- Permite preparação antes do início da gravação

### Pasta de Salvamento
- Pasta padrão: `AudioScreenRecorder` 
- Personalizável nas configurações
- Localizada em: `/storage/emulated/0/Android/data/com.audioscreenrecorder/files/`

### Formato de Áudio
- MP4 (padrão)
- AAC
- 3GP

## Desenvolvimento

### Estrutura do Projeto
```
AudioScreenRecorder/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/audioscreenrecorder/
│   │   │   │   ├── recorder/         # Lógica de gravação
│   │   │   │   ├── ui/               # Interface do usuário
│   │   │   │   ├── utils/            # Utilitários
│   │   │   │   └── SettingsPreferences.kt
│   │   │   ├── res/                  # Recursos (layouts, strings, etc.)
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                     # Testes unitários
│   │   └── androidTest/              # Testes instrumentados
│   └── build.gradle
├── .github/
│   └── workflows/
│       └── build-and-deploy.yml      # CI/CD
├── build.gradle
├── settings.gradle
└── README.md
```

### Tecnologias Utilizadas
- **Kotlin** - Linguagem principal
- **Android SDK** - Plataforma
- **MediaRecorder API** - Gravação de áudio
- **MediaProjection API** - Captura de tela
- **Material Design** - Interface do usuário
- **SharedPreferences** - Armazenamento de configurações
- **GitHub Actions** - CI/CD

### Build Local

```bash
# Clone o repositório
git clone https://github.com/camillanapoles/AudioScreenRecorder.git
cd AudioScreenRecorder

# Build debug
./gradlew assembleDebug

# Build release
./gradlew assembleRelease

# Executar testes
./gradlew test
```

### CI/CD

O projeto usa GitHub Actions para:
- Build automático em push/PR
- Execução de testes
- Geração de APKs debug e release
- Upload de artefatos

## Limitações

- Gravação de áudio interno pode não funcionar em todos os dispositivos sem root
- Alguns dispositivos podem requerer permissões adicionais via ADB
- A qualidade da gravação depende do hardware do dispositivo

## Solução de Problemas

### Erro de Permissão
- Verifique se todas as permissões foram concedidas
- Para gravação de áudio interno, alguns dispositivos podem necessitar de root

### Erro ao Iniciar Gravação
- Verifique o espaço de armazenamento disponível
- Certifique-se de que a pasta de salvamento existe
- Reinicie o aplicativo

### Gravação de Tela Não Funciona
- Aceite a permissão MediaProjection quando solicitado
- Verifique se o modo está configurado para "Áudio e Tela"

## Contribuindo

Contribuições são bem-vindas! Por favor:
1. Faça fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFuncionalidade`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova funcionalidade'`)
4. Push para a branch (`git push origin feature/NovaFuncionalidade`)
5. Abra um Pull Request

## Licença

Este projeto é de código aberto e está disponível sob a licença MIT.

## Autor

Camilla Napoles

## Suporte

Para problemas ou dúvidas, abra uma issue no GitHub.
