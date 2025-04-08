import { Text, SafeAreaView } from 'react-native'

export default function LoadingPage() {
    return(
        <SafeAreaView style={{backgroundColor:'#eee1b7', flex: 1, justifyContent:'center'}}>
            <Text style={{textAlign:'center'}}>
                로딩화면..🐜🐜🐜
            </Text>
        </SafeAreaView>
    )
}