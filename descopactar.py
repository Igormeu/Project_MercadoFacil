import zipfile

# Abre o arquivo ZIP no modo de leitura
with zipfile.ZipFile('Versão 2 - Projeto.zip', 'r') as zip_ref:
    # Extrai todos os arquivos para o diretório especificado
    zip_ref.extractall('diretorio_destino')
    print("Arquivo ZIP descompactado com sucesso!")   