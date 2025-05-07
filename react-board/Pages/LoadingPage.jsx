import { Text, SafeAreaView,StyleSheet } from 'react-native'

export default function LoadingPage() {
    return(
        <SafeAreaView style={loadingStyle.container}>
            <Text style={loadingStyle.splashText}>
                Loading My Tip🍯
            </Text>
        </SafeAreaView>
    )
}

const loadingStyle = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#FFFCF3',
        justifyContent:'center'
    },
    splashText: {
        textAlign:'center',
        color: '#2D5652',
        fontSize: 20
        }
})