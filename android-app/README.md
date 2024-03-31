![preview](image.png)

Android Studio Canary에서 제공하는 "gemini starter" 프로젝트 템플릿으로 만들어본 간단한 앱입니다.

# 동작
1. 나라를 선택하면
2. 프롬프트 템플릿 + 사용자 입력값을 조합하고
3. Gemini를 통해 응답 생성
4. 응답 보여주기

# 프롬프트
[Few-shot 프롬프트 예시 - ai.google.dev](https://ai.google.dev/docs/concepts?hl=ko#prompt101)
```
val prompt = """
    Italy: Rome
    Germany: Berlin
    France: Paris
    $country:   
""".trimIndent()
```

# How to run

## 1. Gemini API Key 발급
[Google AI Studio에서 발급 가능]("https://aistudio.google.com/app/apikey")

## 2. local.properties 업데이트
```properties
apiKey=AIzaSyAZ0.........
```
